package com.kaleert.nyagram.callback;

import com.kaleert.nyagram.api.methods.AnswerCallbackQuery;
import com.kaleert.nyagram.api.objects.Update;
import com.kaleert.nyagram.callback.annotation.CallbackVar;
import com.kaleert.nyagram.command.CommandContext;
import com.kaleert.nyagram.core.ArgumentResolver;
import com.kaleert.nyagram.core.CommandResult;
import com.kaleert.nyagram.exceptions.ArgumentParseException;
import com.kaleert.nyagram.i18n.*;
import com.kaleert.nyagram.ui.UIRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Диспетчер callback-запросов (нажатий на кнопки Inline-клавиатуры).
 * <p>
 * Отвечает за:
 * 1. Получение данных из {@code callback_query}.
 * 2. Поиск подходящего обработчика в {@link CallbackRegistry} по шаблону.
 * 3. Парсинг переменных пути (например, {@code id} из {@code item:{id}}).
 * 4. Вызов метода-обработчика и автоматический ответ на callback (убирание часиков).
 * </p>
 *
 * @since 1.0.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CallbackDispatcher {

    private final CallbackRegistry registry;
    private final CallbackPatternParser parser;
    private final List<ArgumentResolver<?>> resolvers;
    private final Optional<LocaleService> localeService;
    private final Optional<LocaleResolver> localeResolver;
    private final UIRegistry uiRegistry;
    
    /**
     * Маршрутизирует входящий callback-запрос к соответствующему методу-обработчику.
     * <p>
     * Если обработчик найден, метод извлекает переменные из пути, инъектирует аргументы
     * и вызывает метод. Если метод возвращает строку или {@link CommandResult},
     * автоматически отправляется {@code answerCallbackQuery} с этим текстом.
     * </p>
     *
     * @param context Контекст команды, содержащий Update с callback_query.
     */
    public void dispatch(CommandContext context) {
        Update update = context.getUpdate();
        if (update.getCallbackQuery() == null) return;

        String data = update.getCallbackQuery().getData();
        
        if (data.startsWith("ui:")) {
            String[] parts = data.split(":");
            if (parts.length >= 3) {
                String menuId = parts[1];
                String methodName = parts[2];
                
                Object menuBean = uiRegistry.getMenuBean(menuId);
                if (menuBean != null) {
                    try {
                        Method targetMethod = menuBean.getClass().getDeclaredMethod(methodName, CommandContext.class);
                        targetMethod.setAccessible(true);
                        Object result = targetMethod.invoke(menuBean, context);
                        handleResult(context, result);
                        return;
                    } catch (Exception e) {
                        log.error("Failed to invoke UI Menu method {} for menu {}", methodName, menuId, e);
                    }
                }
            }
        }
        
        CallbackMeta meta = registry.findMatch(data);

        if (meta == null) {
            log.warn("No handler found for callback: {}", data);
            return;
        }

        try {
            Map<String, String> variables = parser.extractVariables(meta.getPattern(), data);
            
            Object[] args = resolveArguments(meta, context, variables);
            Object result = meta.getMethod().invoke(meta.getBean(), args);
            
            handleResult(context, result);

        } catch (Exception e) {
            log.error("Error executing callback handler: {}", data, e);
            try {
                context.getClient().execute(AnswerCallbackQuery.builder()
                        .callbackQueryId(update.getCallbackQuery().getId())
                        .text("❌ Ошибка обработки")
                        .showAlert(true)
                        .build());
            } catch (Exception ignored) {}
        }
    }

    private Object[] resolveArguments(CallbackMeta meta, CommandContext context, Map<String, String> variables) {
        Parameter[] parameters = meta.getMethod().getParameters();
        Object[] args = new Object[parameters.length];

        for (int i = 0; i < parameters.length; i++) {
            Parameter param = parameters[i];
            
            if (param.isAnnotationPresent(CallbackVar.class)) {
                CallbackVar varAnn = param.getAnnotation(CallbackVar.class);
                String varName = varAnn.value().isEmpty() ? param.getName() : varAnn.value();
                String rawValue = variables.get(varName);
                
                args[i] = convertValue(param, rawValue, context);
                continue;
            }

            ArgumentResolver<?> resolver = resolvers.stream()
                    .filter(r -> r.supports(param))
                    .findFirst()
                    .orElse(null);

            if (resolver != null) {
                args[i] = resolver.resolve(context, param, null); 
            } else {
                args[i] = null;
            }
        }
        return args;
    }

    private Object convertValue(Parameter param, String rawValue, CommandContext context) {
        if (rawValue == null) return null;
        
        return resolvers.stream()
                .filter(r -> r.supports(param))
                .findFirst()
                .map(r -> r.resolve(context, param, rawValue))
                .orElseThrow(() -> new ArgumentParseException("Cannot convert callback variable to " + param.getType().getSimpleName()));
    }

    private void handleResult(CommandContext context, Object result) {
        String callbackId = context.getUpdate().getCallbackQuery().getId();
        
        if (result instanceof String text) {
            context.getClient().execute(AnswerCallbackQuery.builder()
                    .callbackQueryId(callbackId)
                    .text(text)
                    .showAlert(false)
                    .build());
        } else if (result instanceof CommandResult cr) {
             context.getClient().execute(AnswerCallbackQuery.builder()
                    .callbackQueryId(callbackId)
                    .text(cr.getMessage())
                    .showAlert(!cr.isSuccess())
                    .build());
        } else {
            context.getClient().execute(AnswerCallbackQuery.builder()
                    .callbackQueryId(callbackId)
                    .build());
        }
    }
}