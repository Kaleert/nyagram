package pro.kaleert.nyagram.api.methods.groupadministration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import pro.kaleert.nyagram.api.exception.TelegramApiRequestException;
import pro.kaleert.nyagram.api.exception.TelegramApiValidationException;
import pro.kaleert.nyagram.api.meta.BotApiMethod;
import pro.kaleert.nyagram.api.objects.boost.UserChatBoosts;
import lombok.*;

/**
 * Получает список бустов, добавленных конкретным пользователем в чат.
 *
 * @since 1.2.2
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetUserChatBoosts extends BotApiMethod<UserChatBoosts> {
    public static final String PATH = "getUserChatBoosts";

    @JsonProperty("chat_id")
    private String chatId;

    @JsonProperty("user_id")
    private Long userId;

    @Override
    public String getMethod() { return PATH; }

    @Override
    public UserChatBoosts deserializeResponse(String answer) throws TelegramApiRequestException {
        return deserializeResponse(answer, UserChatBoosts.class);
    }

    @Override
    public void validate() throws TelegramApiValidationException {
        if (chatId == null || chatId.isEmpty()) throw new TelegramApiValidationException("ChatId is required", PATH);
        if (userId == null) throw new TelegramApiValidationException("UserId is required", PATH);
    }
}
