package com.kaleert.nyagram.api.methods.updatingmessages;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kaleert.nyagram.api.exception.TelegramApiRequestException;
import com.kaleert.nyagram.api.exception.TelegramApiValidationException;
import com.kaleert.nyagram.api.meta.BotApiMethod;
import com.kaleert.nyagram.api.objects.replykeyboard.InlineKeyboardMarkup;
import lombok.*;

import java.io.Serializable;

/**
 * Используйте этот метод для редактирования только inline-клавиатуры сообщений.
 *
 * @since 1.1.4
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EditMessageReplyMarkup extends BotApiMethod<Serializable> {

    public static final String PATH = "editMessageReplyMarkup";

    @JsonProperty("chat_id")
    private String chatId;

    @JsonProperty("message_id")
    private Integer messageId;

    @JsonProperty("inline_message_id")
    private String inlineMessageId;

    @JsonProperty("reply_markup")
    private InlineKeyboardMarkup replyMarkup;

    @Override
    public String getMethod() {
        return PATH;
    }

    @Override
    public Serializable deserializeResponse(String answer) throws TelegramApiRequestException {
        try {
            return deserializeResponse(answer, com.kaleert.nyagram.api.objects.message.Message.class);
        } catch (TelegramApiRequestException e) {
            return deserializeResponse(answer, Boolean.class);
        }
    }

    @Override
    public void validate() throws TelegramApiValidationException {
        boolean hasChat = (chatId != null && messageId != null);
        boolean hasInline = (inlineMessageId != null);

        if (!hasChat && !hasInline) {
            throw new TelegramApiValidationException("Must provide either (chatId + messageId) or inlineMessageId", PATH);
        }
    }
}