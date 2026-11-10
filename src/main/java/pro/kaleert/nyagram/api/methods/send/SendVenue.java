package pro.kaleert.nyagram.api.methods.send;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import pro.kaleert.nyagram.api.exception.TelegramApiRequestException;
import pro.kaleert.nyagram.api.exception.TelegramApiValidationException;
import pro.kaleert.nyagram.api.meta.BotApiMethod;
import pro.kaleert.nyagram.api.objects.message.Message;
import pro.kaleert.nyagram.api.objects.message.EphemeralMessageParameters;
import pro.kaleert.nyagram.api.objects.ReplyParameters;
import pro.kaleert.nyagram.api.objects.replykeyboard.ReplyKeyboard;
import lombok.*;

/**
 * Используйте этот метод для отправки информации о месте (заведении) на карте.
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
public class SendVenue extends BotApiMethod<Message> {

    /** Имя метода Telegram Bot API: {@code sendVenue}. */
    public static final String PATH = "sendVenue";

    /** Идентификатор целевого чата или имя публичного канала. */
    @JsonProperty("chat_id")
    private String chatId;

    /** Идентификатор топика в супергруппе-форуме. */
    @JsonProperty("message_thread_id")
    private Integer messageThreadId;

    /** Широта в градусах. */
    @JsonProperty("latitude")
    private Double latitude;

    /** Долгота в градусах. */
    @JsonProperty("longitude")
    private Double longitude;

    /** Название места или заведения. */
    @JsonProperty("title")
    private String title;

    /** Почтовый адрес места или заведения. */
    @JsonProperty("address")
    private String address;

    /** Идентификатор места в Foursquare. */
    @JsonProperty("foursquare_id")
    private String foursquareId;

    /** Тип места по классификации Foursquare. */
    @JsonProperty("foursquare_type")
    private String foursquareType;

    /** Идентификатор места в Google Places. */
    @JsonProperty("google_place_id")
    private String googlePlaceId;

    /** Тип места по классификации Google Places. */
    @JsonProperty("google_place_type")
    private String googlePlaceType;

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
     * Устаревший флаг: разрешает отправку без доступного исходного сообщения. Предпочтительнее replyParameters.
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
        if (((chatId == null || chatId.isEmpty()) && !(ephemeralMessageParameters != null
                && ephemeralMessageParameters.callbackQueryId() != null
                && !ephemeralMessageParameters.callbackQueryId().isBlank())) /* BOTAPI104_EPHEMERAL_CHAT */) throw new TelegramApiValidationException("ChatId is required", PATH);
        if (latitude == null) throw new TelegramApiValidationException("Latitude is required", PATH);
        if (longitude == null) throw new TelegramApiValidationException("Longitude is required", PATH);
        if (title == null || title.isEmpty()) throw new TelegramApiValidationException("Title is required", PATH);
        if (address == null || address.isEmpty()) throw new TelegramApiValidationException("Address is required", PATH);
    }
    
    /**
     * Разрешить платную рассылку этого сообщения (0.1 Star за сообщение).
     * @return текущий билдер.
     * @since 1.2.0
     */
    public SendVenue paidBroadcast() {
        this.allowPaidBroadcast = true;
        return this;
    }
}