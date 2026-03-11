package com.kaleert.nyagram.api.methods.send;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kaleert.nyagram.api.exception.TelegramApiRequestException;
import com.kaleert.nyagram.api.exception.TelegramApiValidationException;
import com.kaleert.nyagram.api.meta.BotApiMethod;
import com.kaleert.nyagram.api.objects.MessageId;
import lombok.*;

import java.util.List;

/**
 * Копирует сразу несколько сообщений (до 100).
 *
 * @since 1.1.4
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class CopyMessages extends BotApiMethod<List<MessageId>> {
    public static final String PATH = "copyMessages";

    @JsonProperty("chat_id") private String chatId;
    @JsonProperty("message_thread_id") private Integer messageThreadId;
    @JsonProperty("from_chat_id") private String fromChatId;
    @JsonProperty("message_ids") private List<Integer> messageIds;
    @JsonProperty("disable_notification") private Boolean disableNotification;
    @JsonProperty("protect_content") private Boolean protectContent;
    @JsonProperty("remove_caption") private Boolean removeCaption;

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
}