package pro.kaleert.nyagram.api.methods.forum;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import pro.kaleert.nyagram.api.exception.TelegramApiValidationException;
import pro.kaleert.nyagram.api.meta.BotApiMethodBoolean;
import lombok.*;

/**
 * Используйте этот метод для изменения названия основного топика "General" в супергруппе.
 *
 * @since 1.2.2
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EditGeneralForumTopic extends BotApiMethodBoolean {
    public static final String PATH = "editGeneralForumTopic";

    @JsonProperty("chat_id")
    private String chatId;

    @JsonProperty("name")
    private String name;

    @Override
    public String getMethod() { return PATH; }

    @Override
    public void validate() throws TelegramApiValidationException {
        if (chatId == null || chatId.isEmpty()) throw new TelegramApiValidationException("ChatId is required", PATH);
        if (name == null || name.isEmpty() || name.length() > 128) throw new TelegramApiValidationException("Name must be 1-128 chars", PATH);
    }
}
