package pro.kaleert.nyagram.api.methods.send;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import pro.kaleert.nyagram.api.exception.TelegramApiRequestException;
import pro.kaleert.nyagram.api.exception.TelegramApiValidationException;
import pro.kaleert.nyagram.api.meta.BotApiMethod;
import pro.kaleert.nyagram.api.objects.ReplyParameters;
import pro.kaleert.nyagram.api.objects.message.Message;
import pro.kaleert.nyagram.api.objects.replykeyboard.ReplyKeyboard;
import lombok.*;

/**
 * Используйте этот метод для отправки анимированного эмодзи, который будет отображать случайное значение.
 *
 * @since 1.1.4
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SendDice extends BotApiMethod<Message> {

    /** Имя метода Telegram Bot API: {@code sendDice}. */
    public static final String PATH = "sendDice";

    /** Идентификатор целевого чата или имя публичного канала. */
    @JsonProperty("chat_id")
    private String chatId;

    /** Идентификатор топика в супергруппе-форуме. */
    @JsonProperty("message_thread_id")
    private Integer messageThreadId;

    /**
     * Эмодзи. Поддерживаются: 🎲 (по умолчанию), 🎯, 🏀, ⚽, 🎳, 🎰.
     */
    @JsonProperty("emoji")
    private String emoji;

    /** Отправить сообщение без звукового уведомления. */
    @JsonProperty("disable_notification")
    private Boolean disableNotification;

    /** Запретить пересылку и сохранение содержимого сообщения. */
    @JsonProperty("protect_content")
    private Boolean protectContent;

    /** Идентификатор визуального эффекта сообщения. */
    @JsonProperty("message_effect_id")
    private String messageEffectId;

    /**
     * Если сообщение является ответом, ID исходного сообщения.
     * @deprecated Используйте {@link #replyParameters} начиная с Nyagram 1.1.5
     */
    @Deprecated(since = "1.1.5")
    @JsonProperty("reply_to_message_id")
    private Integer replyToMessageId;

    /**
     * Устаревший флаг: разрешает отправку анимированного эмодзи без найденного сообщения для ответа.
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

    /** Клавиатура или иная разметка ответа. */
    @JsonProperty("reply_markup")
    private ReplyKeyboard replyMarkup;

    /** Идентификатор бизнес-подключения для отправки от его имени. */
    @JsonProperty("business_connection_id")
    private String businessConnectionId;
    
    /**
     * Позволяет отправить сообщение даже тем пользователям, которые заблокировали бота, 
     * за счет списания Telegram Stars с баланса бота.
     * @since 1.2.0
     */
    @JsonProperty("allow_paid_broadcast")
    private Boolean allowPaidBroadcast;

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
        if (chatId == null || chatId.isEmpty()) throw new TelegramApiValidationException("ChatId is required", PATH);
    }
    
    /**
     * Разрешить платную рассылку этого сообщения (0.1 Star за сообщение).
     * @return текущий билдер.
     * @since 1.2.0
     */
    public SendDice paidBroadcast() {
        this.allowPaidBroadcast = true;
        return this;
    }
}