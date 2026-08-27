package pro.kaleert.nyagram.api.methods.send;

import com.fasterxml.jackson.annotation.JsonProperty;
import pro.kaleert.nyagram.api.exception.TelegramApiRequestException;
import pro.kaleert.nyagram.api.exception.TelegramApiValidationException;
import pro.kaleert.nyagram.api.meta.BotApiMethod;
import pro.kaleert.nyagram.api.methods.ParseMode;
import pro.kaleert.nyagram.api.objects.LinkPreviewOptions;
import pro.kaleert.nyagram.api.objects.ReplyParameters;
import pro.kaleert.nyagram.api.objects.message.MessageEntity;
import pro.kaleert.nyagram.api.objects.message.EphemeralMessageParameters;
import pro.kaleert.nyagram.api.objects.message.Message;
import pro.kaleert.nyagram.api.objects.replykeyboard.ReplyKeyboard;
import lombok.*;

import java.util.List;

/**
 * Используйте этот метод для отправки текстовых сообщений.
 * <p>
 * Поддерживает форматирование (HTML, Markdown), отключение предпросмотра ссылок
 * и прикрепление клавиатур.
 * </p>
 *
 * @see pro.kaleert.nyagram.api.methods.ParseMode
 * @since 1.0.0
 */
@EqualsAndHashCode(callSuper = false)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendMessage extends BotApiMethod<Message> {
    
    /** Имя метода в Telegram Bot API. */
    public static final String PATH = "sendMessage";
    
    /**
     * Уникальный идентификатор целевого чата или username целевого канала (в формате {@code @channelusername}).
     */
    @JsonProperty("chat_id")
    private String chatId;
    
    /**
     * Уникальный идентификатор топика (Message Thread ID).
     * Обязателен, если сообщение отправляется в конкретный топик супергруппы-форума.
     */
    @JsonProperty("message_thread_id")
    private Integer messageThreadId;
    
    /**
     * Текст отправляемого сообщения (1-4096 символов).
     */
    @JsonProperty("text")
    private String text;
    
    /**
     * Режим парсинга сущностей в тексте сообщения.
     * <p>
     * Возможные значения: "MarkdownV2", "HTML", "Markdown".
     * Рекомендуется использовать {@link pro.kaleert.nyagram.api.methods.ParseMode}.
     * </p>
     */
    @JsonProperty("parse_mode")
    private String parseMode;

    @JsonProperty("entities")
    private List<MessageEntity> entities;

    @JsonProperty("link_preview_options")
    private LinkPreviewOptions linkPreviewOptions;
    
    /**
     * Отключает уведомление о сообщении.
     * Пользователи получат уведомление без звука.
     */
    @JsonProperty("disable_notification")
    private Boolean disableNotification;
    
    /**
     * Защищает содержимое отправленного сообщения от пересылки и сохранения.
     */
    @JsonProperty("protect_content")
    private Boolean protectContent;
    
    /**
     * Если сообщение является ответом, ID исходного сообщения.
     * @deprecated Используйте {@link #replyParameters} начиная с Nyagram 1.1.5
     */
    @Deprecated(since = "1.1.5")
    @JsonProperty("reply_to_message_id")
    private Integer replyToMessageId;

    /**
     * @deprecated Используйте {@link #replyParameters} начиная с Nyagram 1.1.5
     */
    @Deprecated(since = "1.1.5")
    @JsonProperty("allow_sending_without_reply")
    private Boolean allowSendingWithoutReply;
    
    /**
     * Параметры ответа. Заменяет reply_to_message_id.
     * Необходим для ответов на варианты опросов (API 9.6).
     */
    @JsonProperty("reply_parameters")
    private ReplyParameters replyParameters;
    
    /**
     * Дополнительные возможности интерфейса.
     * Объект, представляющий Inline-клавиатуру, обычную клавиатуру (ReplyKeyboard) или удаление клавиатуры.
     */
    @JsonProperty("reply_markup")
    private ReplyKeyboard replyMarkup;

    /** Идентификатор эффекта сообщения (например, ID анимации огня или сердечек). */
    @JsonProperty("message_effect_id")
    private String messageEffectId;

    /** Идентификатор бизнес-соединения. */
    @JsonProperty("business_connection_id")
    private String businessConnectionId;
    
    /**
     * Позволяет отправить сообщение даже тем пользователям, которые заблокировали бота, 
     * за счет списания Telegram Stars с баланса бота.
     * @since 1.2.0
     */
    @JsonProperty("allow_paid_broadcast")
    private Boolean allowPaidBroadcast;
    
    /** Параметры для эфемерных сообщений.
    *
    * @since 1.2.2
    */
    @JsonProperty("ephemeral_message_parameters")
    private EphemeralMessageParameters ephemeralMessageParameters;

    @Override
    public String getMethod() {
        return PATH;
    }

    @Override
    public Message deserializeResponse(String answer) throws TelegramApiRequestException {
        return deserializeResponse(answer, Message.class);
    }

    @Override
    public void validate() throws TelegramApiValidationException {
        if (chatId == null || chatId.isEmpty()) {
            throw new TelegramApiValidationException("ChatId cannot be empty", PATH, "chat_id");
        }
        if (text == null || text.isEmpty()) {
            throw new TelegramApiValidationException("Text cannot be empty", PATH, "text");
        }
        if (text.length() > 4096) {
            throw new TelegramApiValidationException("Text is too long (max 4096 chars)", PATH, "text");
        }
        if (replyMarkup != null) {
            replyMarkup.validate();
        }
        if (linkPreviewOptions != null) {
            linkPreviewOptions.validate();
        }
    }
    
    /**
     * Устанавливает уникальный идентификатор чата.
     *
     * @param chatId ID чата (Long).
     */
    public void setChatId(@NonNull Long chatId) {
        this.chatId = chatId.toString();
    }
    
    /**
     * Включает HTML-разметку.
     * Позволяет использовать теги {@code <b>}, {@code <i>}, {@code <a>} и другие.
     *
     * @return текущий билдер.
     */
    public SendMessage enableHtml() {
        this.parseMode = ParseMode.HTML;
        return this;
    }
    
    /**
     * Включает MarkdownV2-разметку.
     * Позволяет использовать расширенный синтаксис Markdown (спойлеры, подчеркивание).
     *
     * @return текущий билдер.
     */
    public SendMessage enableMarkdown() {
        this.parseMode = ParseMode.MARKDOWNV2;
        return this;
    }
    
    /**
     * Отключает генерацию превью ссылки в сообщении.
     * <p>
     * Если в тексте есть ссылки, Telegram обычно создает для них предпросмотр (картинку и заголовок).
     * Этот метод предотвращает это поведение.
     * </p>
     *
     * @return текущий билдер.
     */
    public SendMessage disableWebPagePreview() {
        if (this.linkPreviewOptions == null) {
            this.linkPreviewOptions = new LinkPreviewOptions();
        }
        this.linkPreviewOptions.setIsDisabled(true);
        return this;
    }
    
    /**
     * Устанавливает ID сообщения, на которое нужно ответить (Reply).
     *
     * @param messageId ID исходного сообщения.
     * @return текущий билдер.
     */
    public SendMessage replyTo(Integer messageId) {
        this.replyToMessageId = messageId;
        return this;
    }
    
    /**
     * Разрешить платную рассылку этого сообщения (0.1 Star за сообщение).
     * @return текущий билдер.
     * @since 1.2.0
     */
    public SendMessage paidBroadcast() {
        this.allowPaidBroadcast = true;
        return this;
    }

    /**
     * Добавляет визуальный эффект к сообщению (API 7.3+).
     * @param effectId ID эффекта.
     * @return текущий билдер.
     * 
     * @since 1.2.1
     */
    public SendMessage withEffect(String effectId) {
        this.messageEffectId = effectId;
        return this;
    }
}