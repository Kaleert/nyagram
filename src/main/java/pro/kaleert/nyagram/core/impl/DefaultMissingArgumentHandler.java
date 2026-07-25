package pro.kaleert.nyagram.core.impl;

import pro.kaleert.nyagram.api.methods.send.SendMessage;
import pro.kaleert.nyagram.command.CommandContext;
import pro.kaleert.nyagram.core.spi.MissingArgumentHandler;
import pro.kaleert.nyagram.exceptions.ArgumentParseException;
import pro.kaleert.nyagram.meta.CommandMeta;
import pro.kaleert.nyagram.util.TextUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * Стандартный обработчик ситуаций, когда пользователь не указал обязательный аргумент команды.
 * <p>
 * Отправляет пользователю сообщение с ошибкой и примером правильного использования (синтаксисом).
 * </p>
 *
 * @since 1.0.0
 */
@Slf4j
public class DefaultMissingArgumentHandler implements MissingArgumentHandler {

    @Override
    public void handle(CommandContext context, CommandMeta meta, ArgumentParseException e) {
        String syntax = meta.getUsageSyntax();
        String reply = String.format("⚠️ <b>Ошибка ввода:</b> %s\n\n📝 Пример использования:\n%s",
                TextUtil.escapeHtml(e.getMessage()), TextUtil.code(syntax));
        
        try {
            SendMessage msg = SendMessage.builder()
                .chatId(context.getChatId().toString())
                .text(reply)
                .parseMode("HTML")
                .build();
            
            context.getClient().execute(msg);
        } catch (Exception ex) {
            log.warn("Failed to send default missing argument response", ex);
        }
    }
}
