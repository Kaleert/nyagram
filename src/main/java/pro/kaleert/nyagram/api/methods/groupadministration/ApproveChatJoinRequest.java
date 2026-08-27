package pro.kaleert.nyagram.api.methods.groupadministration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import pro.kaleert.nyagram.api.exception.TelegramApiValidationException;
import pro.kaleert.nyagram.api.meta.BotApiMethodBoolean;
import lombok.*;

/**
 * Используйте этот метод для одобрения заявки пользователя на вступление в чат.
 * <p>
 * Бот должен быть администратором в чате с правом can_invite_users.
 * </p>
 *
 * @since 1.2.2
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApproveChatJoinRequest extends BotApiMethodBoolean {
    public static final String PATH = "approveChatJoinRequest";

    @JsonProperty("chat_id")
    private String chatId;

    @JsonProperty("user_id")
    private Long userId;

    @Override
    public String getMethod() { return PATH; }

    @Override
    public void validate() throws TelegramApiValidationException {
        if (chatId == null || chatId.isEmpty()) throw new TelegramApiValidationException("ChatId is required", PATH);
        if (userId == null) throw new TelegramApiValidationException("UserId is required", PATH);
    }
}
