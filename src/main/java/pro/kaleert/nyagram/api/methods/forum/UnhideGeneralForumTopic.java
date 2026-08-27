package pro.kaleert.nyagram.api.methods.forum;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import pro.kaleert.nyagram.api.exception.TelegramApiValidationException;
import pro.kaleert.nyagram.api.meta.BotApiMethodBoolean;
import lombok.*;

/**
 * Используйте этот метод для отмены скрытия основного топика "General" в супергруппе.
 *
 * @since 1.2.2
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UnhideGeneralForumTopic extends BotApiMethodBoolean {
    public static final String PATH = "unhideGeneralForumTopic";

    @JsonProperty("chat_id")
    private String chatId;

    @Override
    public String getMethod() { return PATH; }

    @Override
    public void validate() throws TelegramApiValidationException {
        if (chatId == null || chatId.isEmpty()) throw new TelegramApiValidationException("ChatId is required", PATH);
    }
}
