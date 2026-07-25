package pro.kaleert.nyagram.api.methods.send;

import com.fasterxml.jackson.annotation.JsonProperty;
import pro.kaleert.nyagram.api.exception.TelegramApiRequestException;
import pro.kaleert.nyagram.api.exception.TelegramApiValidationException;
import pro.kaleert.nyagram.api.meta.BotApiMethod;
import pro.kaleert.nyagram.api.objects.MessageId;
import lombok.*;

import java.util.List;

/**
 * Пересылает сразу несколько сообщений (до 100).
 *
 * @since 1.1.4
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ForwardMessages extends BotApiMethod<List<MessageId>> {
    public static final String PATH = "forwardMessages";

    @JsonProperty("chat_id") private String chatId;
    @JsonProperty("message_thread_id") private Integer messageThreadId;
    @JsonProperty("from_chat_id") private String fromChatId;
    @JsonProperty("message_ids") private List<Integer> messageIds;
    @JsonProperty("disable_notification") private Boolean disableNotification;
    @JsonProperty("protect_content") private Boolean protectContent;
    
    /**
     * Позволяет отправить сообщение даже тем пользователям, которые заблокировали бота, 
     * за счет списания Telegram Stars с баланса бота.
     * @since 1.2.0
     */
    @JsonProperty("allow_paid_broadcast")
    private Boolean allowPaidBroadcast;

    @Override public String getMethod() { return PATH; }

    @Override
    public List<MessageId> deserializeResponse(String answer) throws TelegramApiRequestException {
        return deserializeResponseArray(answer, MessageId.class);
    }

    @Override
    public void validate() throws TelegramApiValidationException {
        if (chatId == null) throw new TelegramApiValidationException("chat_id required", PATH);
        if (fromChatId == null) throw new TelegramApiValidationException("from_chat_id required", PATH);
        if (messageIds == null || messageIds.isEmpty() || messageIds.size() > 100) 
            throw new TelegramApiValidationException("message_ids must contain 1-100 elements", PATH);
    }
    
    /**
     * Разрешить платную рассылку этого сообщения (0.1 Star за сообщение).
     * @return текущий билдер.
     * @since 1.2.0
     */
    public ForwardMessages paidBroadcast() {
        this.allowPaidBroadcast = true;
        return this;
    }
}
