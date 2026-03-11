package com.kaleert.nyagram.api.methods.updatingmessages;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kaleert.nyagram.api.exception.TelegramApiRequestException;
import com.kaleert.nyagram.api.exception.TelegramApiValidationException;
import com.kaleert.nyagram.api.meta.BotApiMethod;
import com.kaleert.nyagram.api.objects.message.MessageEntity;
import com.kaleert.nyagram.api.objects.replykeyboard.InlineKeyboardMarkup;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * Используйте этот метод для редактирования подписей (caption) к медиа-сообщениям.
 *
 * @since 1.1.4
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EditMessageCaption extends BotApiMethod<Serializable> {

    public static final String PATH = "editMessageCaption";

    @JsonProperty("chat_id")
    private String chatId;

    @JsonProperty("message_id")
    private Integer messageId;

    @JsonProperty("inline_message_id")
    private String inlineMessageId;

    @JsonProperty("caption")
    private String caption;

    @JsonProperty("parse_mode")
    private String parseMode;

    @JsonProperty("caption_entities")
    private List<MessageEntity> captionEntities;

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
        if (hasChat && hasInline) {
            throw new TelegramApiValidationException("Cannot provide both (chatId + messageId) and inlineMessageId", PATH);
        }
    }
}
