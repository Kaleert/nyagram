package pro.kaleert.nyagram.api.methods.updatingmessages;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import pro.kaleert.nyagram.api.exception.TelegramApiRequestException;
import pro.kaleert.nyagram.api.exception.TelegramApiValidationException;
import pro.kaleert.nyagram.api.meta.BotApiMethod;
import pro.kaleert.nyagram.api.objects.message.MessageEntity;
import pro.kaleert.nyagram.api.objects.replykeyboard.InlineKeyboardMarkup;
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

    /** Имя метода Telegram Bot API: {@code editMessageCaption}. */
    public static final String PATH = "editMessageCaption";

    /** Идентификатор целевого чата или имя публичного канала. */
    @JsonProperty("chat_id")
    private String chatId;

    /** Идентификатор сообщения в пределах чата. */
    @JsonProperty("message_id")
    private Integer messageId;

    /** Идентификатор инлайн-сообщения, подпись которого изменяется. */
    @JsonProperty("inline_message_id")
    private String inlineMessageId;

    /** Подпись к медиафайлу. */
    @JsonProperty("caption")
    private String caption;

    /** Режим обработки разметки: HTML, Markdown или MarkdownV2. */
    @JsonProperty("parse_mode")
    private String parseMode;

    /** Сущности форматирования подписи к медиафайлу. */
    @JsonProperty("caption_entities")
    private List<MessageEntity> captionEntities;

    /** Клавиатура или иная разметка ответа. */
    @JsonProperty("reply_markup")
    private InlineKeyboardMarkup replyMarkup;

    @Override
    public String getMethod() {
        return PATH;
    }

    @Override
    public Serializable deserializeResponse(String answer) throws TelegramApiRequestException {
        try {
            return deserializeResponse(answer, pro.kaleert.nyagram.api.objects.message.Message.class);
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
