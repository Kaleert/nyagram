package com.kaleert.nyagram.core.registry;

import com.kaleert.nyagram.api.objects.Update;
import com.kaleert.nyagram.api.objects.User;
import com.kaleert.nyagram.api.objects.message.Message;
import com.kaleert.nyagram.client.NyagramClient;
import com.kaleert.nyagram.command.BotCommand;
import com.kaleert.nyagram.command.CommandArgument;
import com.kaleert.nyagram.command.CommandContext;
import com.kaleert.nyagram.command.CommandHandler;
import com.kaleert.nyagram.core.AsyncMode;
import com.kaleert.nyagram.meta.CommandMeta;
import com.kaleert.nyagram.security.AccessDeniedAction;
import com.kaleert.nyagram.security.LevelRequired;
import com.kaleert.nyagram.security.RateLimit;
import com.kaleert.nyagram.security.RequiresPermission;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Реестр зарегистрированных команд бота.
 * <p>
 * Сканирует бины Spring на наличие аннотаций {@link BotCommand} и {@link CommandHandler}.
 * Сохраняет метаданные команд и предоставляет быстрый поиск нужного обработчика
 * по текстовому триггеру (например, "/start" или "Меню").
 * </p>
 * <p>
 * Поддерживает поиск по принципу "Longest Match First" (наиболее длинное совпадение)
 * для поддержки подкоманд (например, "/settings audio" приоритетнее "/settings").
 * </p>
 *
 * @since 1.0.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CommandRegistry implements BeanPostProcessor {

    @Getter
    private final Map<String, CommandMeta> commandMap = new ConcurrentHashMap<>();
    
    private final List<String> sortedCommandKeys = new ArrayList<>();

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        Class<?> beanClass = bean.getClass();
        
        if (beanClass.getName().contains("$$")) {
            beanClass = beanClass.getSuperclass();
        }

        BotCommand classAnn = AnnotatedElementUtils.findMergedAnnotation(beanClass, BotCommand.class);
        if (classAnn == null) {
            return bean;
        }

        String rootValue = classAnn.value().trim();
        boolean isControllerMode = !StringUtils.hasText(rootValue);

        List<Method> handlers = Arrays.stream(beanClass.getDeclaredMethods())
                .filter(m -> AnnotatedElementUtils.findMergedAnnotation(m, CommandHandler.class) != null)
                .collect(Collectors.toList());

        if (handlers.isEmpty()) {
            return bean;
        }

        if (!isControllerMode && handlers.size() > 1) {
            long defaultHandlersCount = handlers.stream()
                    .map(m -> AnnotatedElementUtils.findMergedAnnotation(m, CommandHandler.class))
                    .filter(h -> h.value().isEmpty())
                    .count();
            
            if (defaultHandlersCount > 1) {
                log.warn("⚠️ Class '{}' defines root command '{}' but has multiple default handlers (empty value). Ambiguous mapping.", 
                        beanClass.getSimpleName(), rootValue);
            }
        }

        for (Method method : handlers) {
            CommandHandler methodAnn = AnnotatedElementUtils.findMergedAnnotation(method, CommandHandler.class);
            registerHandler(bean, method, rootValue, methodAnn, isControllerMode);
        }

        refreshSearchKeys();
        
        return bean;
    }

    private void registerHandler(Object bean, Method method, String root, CommandHandler handler, boolean isController) {
        String methodValue = handler.value().trim();
        Set<String> triggers = new HashSet<>();

        if (isController) {
            if (StringUtils.hasText(methodValue)) {
                triggers.add(normalize(methodValue));
            }
        } else {
            triggers.add(buildPath(root, methodValue));
        }

        for (String alias : handler.aliases()) {
            if (!StringUtils.hasText(alias)) continue;
            
            if (isController) {
                triggers.add(normalize(alias));
            } else {
                if (methodValue.isEmpty()) {
                    triggers.add(normalize(alias)); 
                } else {
                    triggers.add(buildPath(root, alias));
                }
            }
        }

        for (String path : triggers) {
            try {
                if (!method.canAccess(bean)) {
                    method.setAccessible(true);
                }
                
                CommandMeta meta = buildCommandMeta(bean, method, path, handler);
                commandMap.put(path, meta);
                
                log.info("Registered Command: [{}] -> {}#{}", path, bean.getClass().getSimpleName(), method.getName());
            } catch (Exception e) {
                log.error("Failed to register command path: {}", path, e);
            }
        }
    }

    private CommandMeta buildCommandMeta(Object bean, Method method, String fullPath, CommandHandler handler) throws IllegalAccessException {
        MethodHandle methodHandle = MethodHandles.lookup().unreflect(method).bindTo(bean);
        
        String description = handler.description();
        if (!StringUtils.hasText(description)) {
            BotCommand classAnn = bean.getClass().getAnnotation(BotCommand.class);
            if (classAnn != null && StringUtils.hasText(classAnn.description())) {
                description = classAnn.description();
            }
        }

        Set<String> permissions = new HashSet<>();
        RequiresPermission permAnn = AnnotatedElementUtils.findMergedAnnotation(method, RequiresPermission.class);
        if (permAnn != null) {
            permissions.add(permAnn.value());
        }

        CommandMeta.LevelRequirement levelReq;
        LevelRequired levelAnn = AnnotatedElementUtils.findMergedAnnotation(method, LevelRequired.class);
        if (levelAnn != null) {
            levelReq = new CommandMeta.LevelRequirement(levelAnn.min(), levelAnn.max(), levelAnn.deniedAction());
        } else {
            levelReq = new CommandMeta.LevelRequirement(0, Integer.MAX_VALUE, AccessDeniedAction.NOTIFY);
        }

        CommandMeta.RateLimitMeta rateLimit = null;
        RateLimit rateLimitAnn = AnnotatedElementUtils.findMergedAnnotation(method, RateLimit.class);
        if (rateLimitAnn != null) {
            rateLimit = new CommandMeta.RateLimitMeta(
                    rateLimitAnn.calls(),
                    rateLimitAnn.timeWindowSec(),
                    rateLimitAnn.type()
            );
        }

        AsyncMode asyncAnn = AnnotatedElementUtils.findMergedAnnotation(method, AsyncMode.class);
        AsyncMode.Mode asyncMode = (asyncAnn != null) ? asyncAnn.value() : AsyncMode.Mode.SEQUENTIAL;

        Parameter[] parameters = method.getParameters();
        int minArgs = 0;
        int maxArgs = 0;
        StringBuilder usageBuilder = new StringBuilder(fullPath);

        for (int i = 0; i < parameters.length; i++) {
            Parameter param = parameters[i];

            if (isContextParameter(param.getType())) {
                continue;
            }

            if (param.isVarArgs()) {
                CommandArgument argAnn = param.getAnnotation(CommandArgument.class);
                String name = (argAnn != null && StringUtils.hasText(argAnn.value())) ? argAnn.value() : "args";
                usageBuilder.append(" [").append(name).append("...]");
                maxArgs = Integer.MAX_VALUE;
                break;
            }

            CommandArgument argAnn = param.getAnnotation(CommandArgument.class);
            boolean required = (argAnn == null) || argAnn.required();
            String name = (argAnn != null && StringUtils.hasText(argAnn.value())) ? argAnn.value() : param.getName();

            if (required) {
                minArgs++;
                maxArgs++;
                usageBuilder.append(" <").append(name).append(">");
            } else {
                maxArgs++;
                usageBuilder.append(" [").append(name).append("]");
            }
        }

        return CommandMeta.builder()
                .fullCommandPath(fullPath)
                .handlerInstance(bean)
                .method(method)
                .methodHandle(methodHandle)
                .methodParameters(Arrays.asList(parameters))
                .description(description)
                .requiredPermissions(permissions)
                .levelRequirement(levelReq)
                .rateLimitMeta(rateLimit)
                .minArgs(minArgs)
                .maxArgs(maxArgs)
                .usageSyntax(usageBuilder.toString())
                .asyncMode(asyncMode)
                .build();
    }

    /**
     * Поиск команды по введенному тексту.
     * Использует принцип Longest Match First (наиболее длинное совпадение).
     * @param text Текст сообщения (например, "/test parser url")
     * @return Метаданные команды или null
     */
    public CommandMeta findMatch(String text) {
        if (!StringUtils.hasText(text)) {
            return null;
        }

        String searchKey = text.trim().toLowerCase();

        CommandMeta directMatch = commandMap.get(searchKey);
        if (directMatch != null) {
            return directMatch;
        }

        for (String key : sortedCommandKeys) {
            if (key.isEmpty()) continue;
            if (searchKey.startsWith(key + " ")) {
                return commandMap.get(key);
            }
        }
        
        if (commandMap.containsKey("")) {
            return commandMap.get("");
        }

        return null;
    }

    private void refreshSearchKeys() {
        synchronized (sortedCommandKeys) {
            sortedCommandKeys.clear();
            sortedCommandKeys.addAll(commandMap.keySet());
            sortedCommandKeys.sort((k1, k2) -> {
                int lenComp = Integer.compare(k2.length(), k1.length());
                if (lenComp != 0) return lenComp;
                return k1.compareTo(k2);
            });
        }
    }

    private String normalize(String cmd) {
        return cmd.trim().toLowerCase();
    }

    private String buildPath(String root, String sub) {
        if (!StringUtils.hasText(sub)) {
            return normalize(root);
        }
        String cleanSub = sub.trim().replaceAll("^/+", "");
        return (normalize(root) + " " + cleanSub.toLowerCase()).trim();
    }

    private boolean isContextParameter(Class<?> type) {
        return type.isAssignableFrom(CommandContext.class) ||
               type.isAssignableFrom(Update.class) ||
               type.isAssignableFrom(Message.class) ||
               type.isAssignableFrom(User.class) ||
               type.isAssignableFrom(NyagramClient.class);
    }
    
    /**
     * Возвращает список метаданных всех зарегистрированных команд.
     * <p>
     * Используется для генерации меню помощи (/help), логирования или отладки.
     * </p>
     *
     * @return список объектов {@link CommandMeta}.
     */
    public List<CommandMeta> getAllCommands() {
        return new ArrayList<>(commandMap.values());
    }
    
    /**
     * Очищает внутренний кэш ключей поиска команд.
     * <p>
     * Вызывает принудительное обновление списка ключей и их сортировку
     * (по принципу Longest Match First) при следующем запросе команды.
     * Полезно при динамическом добавлении команд в рантайме.
     * </p>
     */
    public void clearCache() {
        refreshSearchKeys();
    }
}