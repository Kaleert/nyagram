package pro.kaleert.nyagram.api.methods.groupadministration;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import pro.kaleert.nyagram.api.exception.TelegramApiValidationException;
import pro.kaleert.nyagram.api.meta.BotApiMethodBoolean;
import lombok.*;

/**
 * Используйте этот метод для установки тега обычного участника в группе или супергруппе.
 * Бот должен быть администратором в чате и иметь право can_manage_tags.
 *
 * @since 1.1.4
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SetChatMemberTag extends BotApiMethodBoolean {
    
    /** Имя метода Telegram Bot API: {@code setChatMemberTag}. */
    public static final String PATH = "setChatMemberTag";

    /** Идентификатор целевого чата или имя публичного канала. */
    @JsonProperty("chat_id")
    private String chatId;

    /** Уникальный идентификатор пользователя Telegram. */
    @JsonProperty("user_id")
    private Long userId;

    /**
     * Новый тег для пользователя (0-16 символов). Эмодзи не разрешены.
     * Если не указан (или пустая строка) — тег будет удален.
     */
    @JsonProperty("tag")
    private String tag;

    @Override
    public String getMethod() {
        return PATH;
    }

    @Override
    public void validate() throws TelegramApiValidationException {
        if (chatId == null || chatId.isEmpty()) throw new TelegramApiValidationException("ChatId required", PATH);
        if (userId == null) throw new TelegramApiValidationException("UserId required", PATH);
        if (tag != null && tag.length() > 16) throw new TelegramApiValidationException("Tag must be <= 16 characters", PATH);
    }
}