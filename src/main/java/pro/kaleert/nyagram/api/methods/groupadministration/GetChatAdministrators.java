package pro.kaleert.nyagram.api.methods.groupadministration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import pro.kaleert.nyagram.api.exception.TelegramApiRequestException;
import pro.kaleert.nyagram.api.exception.TelegramApiValidationException;
import pro.kaleert.nyagram.api.meta.BotApiMethod;
import pro.kaleert.nyagram.api.objects.chatmember.ChatMember;
import lombok.*;

import java.util.List;

/**
 * Используйте этот метод для получения списка администраторов в чате.
 * <p>
 * Возвращает список объектов {@link ChatMember}, содержащих информацию обо всех создателях и администраторах.
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
public class GetChatAdministrators extends BotApiMethod<List<ChatMember>> {
    public static final String PATH = "getChatAdministrators";

    @JsonProperty("chat_id")
    private String chatId;

    @Override
    public String getMethod() { return PATH; }

    @Override
    public List<ChatMember> deserializeResponse(String answer) throws TelegramApiRequestException {
        return deserializeResponseArray(answer, ChatMember.class);
    }

    @Override
    public void validate() throws TelegramApiValidationException {
        if (chatId == null || chatId.isEmpty()) throw new TelegramApiValidationException("ChatId is required", PATH);
    }
}
