package pro.kaleert.nyagram.api.methods.managedbots;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import pro.kaleert.nyagram.api.exception.TelegramApiRequestException;
import pro.kaleert.nyagram.api.exception.TelegramApiValidationException;
import pro.kaleert.nyagram.api.meta.BotApiMethod;
import pro.kaleert.nyagram.api.objects.message.Message;
import lombok.*;

import java.util.List;

/**
 * Получает последние сообщения из личного чата пользователя.
 * Работает только для бизнес-аккаунтов.
 *
 * @since 1.2.2
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetUserPersonalChatMessages extends BotApiMethod<List<Message>> {
    public static final String PATH = "getUserPersonalChatMessages";

    @JsonProperty("user_id") private Long userId;
    @JsonProperty("limit") private Integer limit;
    @JsonProperty("offset_message_id") private Integer offsetMessageId;

    @Override public String getMethod() { return PATH; }

    @Override public List<Message> deserializeResponse(String answer) throws TelegramApiRequestException {
        return deserializeResponseArray(answer, Message.class);
    }

    @Override public void validate() throws TelegramApiValidationException {
        if (userId == null) throw new TelegramApiValidationException("UserId is required", PATH);
    }
}