package pro.kaleert.nyagram.dispatcher;

import pro.kaleert.nyagram.client.NyagramClient;
import pro.kaleert.nyagram.core.ArgumentResolver;
import pro.kaleert.nyagram.command.CommandContext;
import pro.kaleert.nyagram.command.Flag;
import pro.kaleert.nyagram.core.CommandResult;
import pro.kaleert.nyagram.core.concurrency.NyagramExecutor;
import pro.kaleert.nyagram.core.registry.CommandRegistry;
import pro.kaleert.nyagram.core.resolver.TypedArgumentResolver;
import pro.kaleert.nyagram.core.AsyncMode;
import pro.kaleert.nyagram.core.spi.MissingArgumentHandler;
import pro.kaleert.nyagram.event.BotExecutionErrorEvent;
import pro.kaleert.nyagram.exceptions.ArgumentParseException;
import pro.kaleert.nyagram.exceptions.CommandExecutionException;
import pro.kaleert.nyagram.meta.CommandMeta;
import pro.kaleert.nyagram.middleware.MdcMiddleware;
import pro.kaleert.nyagram.middleware.MiddlewareDispatcher;
import pro.kaleert.nyagram.middleware.MiddlewareResult;
import pro.kaleert.nyagram.pipeline.CommandPostProcessor;
import pro.kaleert.nyagram.pipeline.CommandPreProcessor;
import pro.kaleert.nyagram.util.CommandTokenizer;
import pro.kaleert.nyagram.i18n.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import pro.kaleert.nyagram.api.methods.send.SendMessage;
import pro.kaleert.nyagram.api.objects.message.Message;
import pro.kaleert.nyagram.api.objects.Update;
import org.slf4j.MDC;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import jakarta.annotation.PostConstruct;

/**
 * Стандартная реализация диспетчера команд.
 * <p>
 * Основные функции:
 * <ul>
 *     <li>Поиск обработчика по тексту сообщения (через {@link CommandRegistry}).</li>
 *     <li>Запуск цепочки Middleware.</li>
 *     <li>Парсинг аргументов из строки и их инъекция в методы контроллеров.</li>
 *     <li>Вызов методов-обработчиков (через Reflection или MethodHandles).</li>
 *     <li>Обработка результатов и исключений.</li>
 * </ul>
 * </p>
 *
 * @since 1.0.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CommandDispatcherImpl implements CommandDispatcher {

    private final CommandRegistry commandRegistry;
    private final Map<Class<?>, ArgumentResolver<?>> argumentResolversMap = new HashMap<>();
    private ArgumentResolver<?> enumResolver;
    private final List<ArgumentResolver<?>> rawResolvers;
    private final List<CommandPreProcessor> preProcessors;
    private final List<CommandPostProcessor> postProcessors;
    private final MiddlewareDispatcher middlewareDispatcher;
    private final NyagramExecutor taskExecutor;
    private final NyagramClient nyagramClient;
    private final ApplicationEventPublisher eventPublisher;
    private CommandMeta fallbackMeta;
    private final MissingArgumentHandler missingArgumentHandler;
    private final EventDispatcher eventDispatcher;
    private final Optional<LocaleService> localeService;
    private final Optional<LocaleResolver> localeResolver;
    
    /**
     * Инициализирует диспетчер, регистрируя базовые резолверы аргументов.
     */
    @PostConstruct
    public void init() {
        for (ArgumentResolver<?> resolver : rawResolvers) {
            registerResolver(resolver);
        }
        initFallbackMeta();
        log.info("Nyagram Dispatcher initialized with {} type mappings.", argumentResolversMap.size());
    }
    
    private void initFallbackMeta() {
        try {
            Method method = this.getClass().getDeclaredMethod("fallbackHandler", CommandContext.class);
            fallbackMeta = CommandMeta.builder()
                    .fullCommandPath("fallback")
                    .handlerInstance(this)
                    .method(method)
                    .methodHandle(MethodHandles.lookup().unreflect(method).bindTo(this))
                    .methodParameters(Arrays.asList(method.getParameters()))
                    .asyncMode(AsyncMode.Mode.SEQUENTIAL)
                    .requiredPermissions(Collections.emptySet())
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("Failed to init fallback handler", e);
        }
    }

    @SuppressWarnings("unused")
    private CommandResult fallbackHandler(CommandContext context) {
        eventDispatcher.dispatch(context.getUpdate());
        return CommandResult.noResponse();
    }
    
    private void registerResolver(ArgumentResolver<?> resolver) {
        if (resolver instanceof TypedArgumentResolver<?> typedResolver) {
            Set<Class<?>> types = typedResolver.getSupportedTypes();
            
            for (Class<?> type : types) {
                if (type.equals(Enum.class)) {
                    this.enumResolver = resolver;
                } else {
                    argumentResolversMap.put(type, resolver);
                }
            }
            return;
        }

        log.warn("ArgumentResolver {} does not implement TypedArgumentResolver. " +
                 "It will be ignored in fast-path lookup.", resolver.getClass().getSimpleName());
    }
    
    /**
     * Основной метод диспетчеризации.
     * <p>
     * Проверяет, является ли update сообщением, определяет `userId` для шардирования
     * и передает задачу в {@link NyagramExecutor}.
     * </p>
     *
     * @param update Входящее обновление.
     * @return Future с результатом обработки.
     */
    @Override
    public CompletableFuture<CommandResult> dispatch(Update update) {
        if (!isValidCommand(update)) {
            return CompletableFuture.completedFuture(CommandResult.noResponse());
        }

        Long userId = update.getMessage().getFrom().getId();
        CompletableFuture<CommandResult> future = new CompletableFuture<>();

        taskExecutor.execute(userId, () -> {
            try {
                processCommandAsync(update, future);
            } catch (Exception e) {
                log.error("Critical error in command dispatch for user {}", userId, e);
                future.completeExceptionally(e);
            }
        });
        
        return future;
    }

    private boolean isValidCommand(Update update) {
        return update.hasMessage();
    }

    @SuppressWarnings("unused")
    private CommandResult processCommandSync(Update update) {
        Message message = update.getMessage();
        String text = message.hasText() ? message.getText() : null;
        long startTime = System.currentTimeMillis();

        CommandMeta meta = (text != null) ? commandRegistry.findMatch(text) : null;
        if (meta == null) {
            meta = fallbackMeta;
        }
        
        final CommandMeta finalMeta = meta; 

        CommandContext context = new CommandContext(
            update, 
            nyagramClient, 
            localeService.orElse(null), 
            localeResolver.orElse(null)
        );

        if (meta.getAsyncMode() == pro.kaleert.nyagram.core.AsyncMode.Mode.CONCURRENT) {
            CompletableFuture.runAsync(() -> executeCommandLogic(context, finalMeta, startTime), taskExecutor);
		    return CommandResult.noResponse();
        }

        return executeCommandLogic(context, finalMeta, startTime);
    }
    
    private void processCommandAsync(Update update, CompletableFuture<CommandResult> future) {
        Message message = update.getMessage();
        String text = message.hasText() ? message.getText() : null;
        long startTime = System.currentTimeMillis();

        CommandMeta meta = (text != null) ? commandRegistry.findMatch(text) : null;
        
        if (meta == null) {
            meta = fallbackMeta;
        }

        CommandContext context = new CommandContext(
            update, 
            nyagramClient, 
            localeService.orElse(null), 
            localeResolver.orElse(null)
        );
        
        final CommandMeta finalMeta = meta; 

        middlewareDispatcher.dispatch(context, finalMeta)
                .thenCompose(middlewareResult -> {
                    if (!middlewareResult.shouldContinue()) {
                        String msg = middlewareResult.getMessage();
                        return CompletableFuture.completedFuture(
                            msg != null ? CommandResult.success(msg) : CommandResult.noResponse()
                        );
                    }
                    
                    if (middlewareResult.getType() == MiddlewareResult.Type.ERROR) {
                        return CompletableFuture.completedFuture(CommandResult.error(middlewareResult.getMessage()));
                    }

                    if (finalMeta.getAsyncMode() == AsyncMode.Mode.CONCURRENT) {
                        return CompletableFuture.supplyAsync(() -> executeCommandLogic(context, finalMeta, startTime), taskExecutor);
                    } else {
                        return CompletableFuture.completedFuture(executeCommandLogic(context, finalMeta, startTime));
                    }
                })
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        handleException(context, finalMeta, ex);
                        future.complete(CommandResult.error(ex.getMessage()));
                    } else {
                        future.complete(result);
                    }
                });
    }

    private CommandResult executeCommandLogic(CommandContext context, CommandMeta meta, long startTime) {
        CommandResult result = null;
        try {
            for (CommandPreProcessor preProcessor : preProcessors) {
                Optional<CommandResult> preResult = preProcessor.process(context, meta);
                if (preResult.isPresent()) {
                    result = preResult.get();
                    sendResult(context, result);
                    return result;
                }
            }

            Object invocationResult;
            if (meta == fallbackMeta) {
                invocationResult = fallbackHandler(context);
            } else {
                Object[] args = resolveArguments(meta, context.getText(), context);
                if (meta.getMethodHandle() != null) {
                    invocationResult = meta.getMethodHandle().invokeWithArguments(args);
                } else {
                    invocationResult = meta.getMethod().invoke(meta.getHandlerInstance(), args);
                }
            }

            result = processInvocationResult(invocationResult);
            sendResult(context, result);

        } catch (InvocationTargetException e) {
            result = handleException(context, meta, e.getTargetException());
            sendResult(context, result);
        } catch (Throwable e) {
            result = handleException(context, meta, e);
            sendResult(context, result);
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            final CommandResult finalResult = (result != null) ? result : CommandResult.error("Unknown state");
            
            for (CommandPostProcessor postProcessor : postProcessors) {
                try {
                    postProcessor.process(context, finalResult, duration);
                } catch (Exception e) {
                    log.error("Error in PostProcessor {}", postProcessor.getClass().getSimpleName(), e);
                }
            }
        }
        return result;
    }

    private Object[] resolveArguments(CommandMeta meta, String fullText, CommandContext context) {
        String commandPath = meta.getFullCommandPath();
        String argsString = fullText.length() >= commandPath.length()
                ? fullText.substring(commandPath.length()).trim()
                : "";

        List<String> rawTokens = CommandTokenizer.tokenize(argsString);
        List<Parameter> parameters = meta.getMethodParameters();
        
        boolean[] flagValues = new boolean[parameters.size()];
        for (int i = 0; i < parameters.size(); i++) {
            Parameter param = parameters.get(i);
            Flag flagAnn = param.getAnnotation(Flag.class);
            if (flagAnn != null) {
                String flagName = flagAnn.value();
                String dashFlag = "-" + flagName;
                String doubleDashFlag = "--" + flagName;
                
                boolean found = false;
                for (int j = 0; j < rawTokens.size(); j++) {
                    if (rawTokens.get(j).equals(dashFlag) || rawTokens.get(j).equals(doubleDashFlag)) {
                        found = true;
                        rawTokens.remove(j);
                        break;
                    }
                }
                flagValues[i] = found;
            }
        }

        Queue<String> tokens = new ArrayDeque<>(rawTokens);
        Object[] invokeArgs = new Object[parameters.size()];

        for (int i = 0; i < parameters.size(); i++) {
            Parameter param = parameters.get(i);

            if (param.isAnnotationPresent(Flag.class)) {
                invokeArgs[i] = flagValues[i];
                continue;
            }

            if (param.isVarArgs()) {
                invokeArgs[i] = resolveVarArgs(param, tokens, context);
                break;
            }

            ArgumentResolver<?> resolver = findResolver(param.getType());

            if (!resolver.isTokenRequired()) {
                invokeArgs[i] = resolver.resolve(context, param, null);
                continue;
            }

            if (param.getType().equals(String.class) && isLastTokenConsumingParam(parameters, i)) {
                if (tokens.isEmpty() && isParameterOptional(param)) {
                    invokeArgs[i] = null;
                } else if (tokens.isEmpty()) {
                    throw new ArgumentParseException("Missing required text argument: " + getParameterName(param));
                } else {
                    List<String> remaining = new ArrayList<>(tokens);
                    invokeArgs[i] = String.join(" ", remaining);
                    tokens.clear();
                }
                continue;
            }

            String token = tokens.poll();
            
            if (token == null) {
                if (isParameterOptional(param)) {
                    invokeArgs[i] = null;
                    continue;
                }
                throw new ArgumentParseException("Missing required argument: " + getParameterName(param));
            }

            invokeArgs[i] = resolver.resolve(context, param, token);
        }
        
        if (!tokens.isEmpty()) {
             throw new ArgumentParseException(
                 String.format("Too many arguments provided. Usage: %s", meta.getUsageSyntax())
             );
        }

        return invokeArgs;
    }
    
    private boolean isLastTokenConsumingParam(List<Parameter> parameters, int currentIndex) {
        for (int i = currentIndex + 1; i < parameters.size(); i++) {
            Parameter p = parameters.get(i);
            if (p.isAnnotationPresent(Flag.class)) {
                continue;
            }
            if (p.isVarArgs()) {
                return false;
            }
            ArgumentResolver<?> resolver = findResolver(p.getType());
            if (resolver.isTokenRequired()) {
                return false;
            }
        }
        return true;
    }
    
    private Object resolveVarArgs(Parameter param, Queue<String> tokens, CommandContext context) {
        Class<?> componentType = param.getType().getComponentType();
        ArgumentResolver<?> componentResolver = findResolver(componentType);

        List<Object> varArgsValues = new ArrayList<>();

        while (!tokens.isEmpty()) {
            String token = tokens.poll();
            try {
                varArgsValues.add(componentResolver.resolve(context, param, token));
            } catch (Exception e) {
                throw new ArgumentParseException(
                    String.format("Error parsing varargs element '%s': %s", token, e.getMessage())
                );
            }
        }

        Object array = java.lang.reflect.Array.newInstance(componentType, varArgsValues.size());
        for (int i = 0; i < varArgsValues.size(); i++) {
            java.lang.reflect.Array.set(array, i, varArgsValues.get(i));
        }

        return array;
    }
    
    @SuppressWarnings("unused")
    private Object convertToken(String token, Parameter param, CommandContext ctx) {
        Class<?> type = param.getType();

        ArgumentResolver<?> resolver = argumentResolversMap.get(type);

        if (resolver == null && type.isEnum()) {
            resolver = enumResolver;
        }

        if (resolver == null) {
            log.error("No resolver registered for type: {}", type.getName());
            throw new CommandExecutionException(
                "System Error: Unsupported argument type " + type.getSimpleName(), null
            );
        }

        try {
            return resolver.resolve(ctx, param, token);
        } catch (ArgumentParseException e) {
            throw e;
        } catch (Exception e) {
            throw new ArgumentParseException("Invalid format for " + type.getSimpleName());
        }
    }
    
    @SuppressWarnings("unused")
    private boolean isTypeSupported(ArgumentResolver<?> resolver, Class<?> type) {
        String resolverName = resolver.getClass().getSimpleName();
        if (resolverName.startsWith("Integer") && (type == Integer.class || type == int.class)) return true;
        if (resolverName.startsWith("Long") && (type == Long.class || type == long.class)) return true;
        if (resolverName.startsWith("Double") && (type == Double.class || type == double.class)) return true;
        if (resolverName.startsWith("String") && type == String.class) return true;
        if (resolverName.startsWith("Boolean") && (type == Boolean.class || type == boolean.class)) return true;
        
        return false;
    }
    
    private ArgumentResolver<?> findResolver(Class<?> type) {
        ArgumentResolver<?> resolver = argumentResolversMap.get(type);
        if (resolver == null && type.isEnum()) {
            return enumResolver;
        }
        if (resolver == null) {
            throw new CommandExecutionException("No resolver found for type: " + type.getSimpleName(), null);
        }
        return resolver;
    }
    
    private boolean isParameterOptional(Parameter param) {
        pro.kaleert.nyagram.command.CommandArgument argAnn = 
            param.getAnnotation(pro.kaleert.nyagram.command.CommandArgument.class);
        return argAnn != null && !argAnn.required();
    }
    
    private String getParameterName(Parameter param) {
        pro.kaleert.nyagram.command.CommandArgument argAnn = 
            param.getAnnotation(pro.kaleert.nyagram.command.CommandArgument.class);
        return (argAnn != null && !argAnn.value().isEmpty()) ? argAnn.value() : param.getName();
    }

    @SuppressWarnings("unused")
    private String reconstructTail(List<String> tokens, int startIndex) {
        StringBuilder tail = new StringBuilder();
        for (int j = startIndex; j < tokens.size(); j++) {
            if (tail.length() > 0) tail.append(" ");
            tail.append(tokens.get(j));
        }
        return tail.toString();
    }

    private CommandResult processInvocationResult(Object result) {
        if (result instanceof CommandResult cr) {
            return cr;
        } else if (result instanceof String text) {
            return CommandResult.success(text);
        } else if (result == null) {
            return CommandResult.noResponse();
        } else {
            log.warn("Command handler returned unexpected type: {}. Returning as string.",
                    result.getClass().getName());
            return CommandResult.success(result.toString());
        }
    }

    private void sendResult(CommandContext context, CommandResult result) {
        if (result != null && 
            result.getMessage() != null && 
            !result.getMessage().isEmpty()) {
            
            context.reply(result.getMessage());
        }
    }

    private CommandResult handleException(CommandContext context, CommandMeta meta, Throwable e) {
        String traceId = MDC.get(MdcMiddleware.TRACE_ID_KEY);
        if (traceId == null) traceId = "unknown";
    
        if (e instanceof ArgumentParseException) {
            missingArgumentHandler.handle(context, meta, (ArgumentParseException) e);
            return CommandResult.noResponse();
        }
    
        boolean isBusinessException = e instanceof ArgumentParseException 
                       || e instanceof pro.kaleert.nyagram.exceptions.NoPermissionException
                                   || e instanceof IllegalArgumentException;
    
        String userMessage;
        
        if (isBusinessException) {
            log.warn("[{}] User input error in {}: {}", traceId, meta.getFullCommandPath(), e.getMessage());
            userMessage = "⚠️ <b>Ошибка:</b> " + e.getMessage();
        } else {
            log.error("[{}] System failure in command {}: {}", traceId, meta.getFullCommandPath(), e.getMessage(), e);
            
            eventPublisher.publishEvent(new BotExecutionErrorEvent(context, e, meta.getFullCommandPath(), traceId));
            
            userMessage = "❌ <b>Внутренняя ошибка.</b>\nID запроса: <code>" + traceId + "</code>";
        }
    
        try {
            SendMessage msg = SendMessage.builder()
                .chatId(context.getChatId().toString())
                .text(userMessage)
                .parseMode("HTML")
                .build();
            nyagramClient.execute(msg);
        } catch (Exception ex) {
            log.warn("[{}] Failed to send error response to user: {}", traceId, ex.getMessage());
        }
        
        return CommandResult.error(e.getMessage());
    }
    
    /**
     * Генерирует текстовую сводку о всех зарегистрированных командах.
     * <p>
     * Формирует отформатированный список путей команд и их описаний.
     * Удобно использовать при старте приложения для вывода в лог.
     * </p>
     *
     * @return Строка с информацией о командах.
     */
    public String getRegisteredCommandsInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("📋 <b>Registered Commands:</b>\n\n");
        
        List<CommandMeta> allCommands = commandRegistry.getAllCommands();
        allCommands.sort((c1, c2) -> c1.getFullCommandPath().compareTo(c2.getFullCommandPath()));
        
        for (CommandMeta meta : allCommands) {
            sb.append("• <code>").append(meta.getFullCommandPath()).append("</code>\n");
            if (!meta.getDescription().isEmpty()) {
                sb.append("  └ ").append(meta.getDescription()).append("\n");
            }
            sb.append("\n");
        }
        
        sb.append("\nTotal: ").append(allCommands.size()).append(" command(s)");
        
        return sb.toString();
    }
}
