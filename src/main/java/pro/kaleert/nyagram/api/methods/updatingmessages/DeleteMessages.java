package pro.kaleert.nyagram.api.methods.updatingmessages;

import com.fasterxml.jackson.annotation.JsonProperty;
import pro.kaleert.nyagram.api.exception.TelegramApiValidationException;
import pro.kaleert.nyagram.api.meta.BotApiMethodBoolean;
import lombok.*;

import java.util.List;

/**
 * Используйте этот метод для удаления сразу нескольких сообщений (от 1 до 100).
 *
 * @since 1.1.4
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class DeleteMessages extends BotApiMethodBoolean {

    /** Имя метода Telegram Bot API: {@code deleteMessages}. */
    public static final String PATH = "deleteMessages";

    /** Идентификатор целевого чата или имя публичного канала. */
    @JsonProperty("chat_id")
    private String chatId;

    /** Идентификаторы сообщений, которые требуется удалить. */
    @JsonProperty("message_ids")
    private List<Integer> messageIds;

    @Override
    public String getMethod() {
        return PATH;
    }

    @Override
    public void validate() throws TelegramApiValidationException {
        if (chatId == null || chatId.isEmpty()) throw new TelegramApiValidationException("ChatId is required", PATH);
        if (messageIds == null || messageIds.isEmpty()) throw new TelegramApiValidationException("MessageIds cannot be empty", PATH);
        if (messageIds.size() > 100) throw new TelegramApiValidationException("Maximum 100 messages can be deleted at once", PATH);
    }
}