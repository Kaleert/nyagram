package pro.kaleert.nyagram.api.methods.inlinequery;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import pro.kaleert.nyagram.api.exception.TelegramApiRequestException;
import pro.kaleert.nyagram.api.exception.TelegramApiValidationException;
import pro.kaleert.nyagram.api.meta.BotApiMethod;
import pro.kaleert.nyagram.api.objects.inlinequery.PreparedInlineMessage;
import pro.kaleert.nyagram.api.objects.inlinequery.result.InlineQueryResult;
import lombok.*;

/**
 * Сохраняет инлайн-сообщение, чтобы его можно было отправить пользователем из Web App.
 * Возвращает PreparedInlineMessage с ID, который вы передаете в JS метод `shareMessage`.
 *
 * @since 1.2.2
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SavePreparedInlineMessage extends BotApiMethod<PreparedInlineMessage> {
    public static final String PATH = "savePreparedInlineMessage";

    @JsonProperty("user_id") private Long userId;
    @JsonProperty("result") private InlineQueryResult result;
    @JsonProperty("allow_user_chats") private Boolean allowUserChats;
    @JsonProperty("allow_bot_chats") private Boolean allowBotChats;
    @JsonProperty("allow_group_chats") private Boolean allowGroupChats;
    @JsonProperty("allow_channel_chats") private Boolean allowChannelChats;

    @Override public String getMethod() { return PATH; }

    @Override public PreparedInlineMessage deserializeResponse(String answer) throws TelegramApiRequestException {
        return deserializeResponse(answer, PreparedInlineMessage.class);
    }

    @Override public void validate() throws TelegramApiValidationException {
        if (userId == null) throw new TelegramApiValidationException("UserId is required", PATH);
        if (result == null) throw new TelegramApiValidationException("Result is required", PATH);
    }
}