package pro.kaleert.nyagram.api.methods.send;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import pro.kaleert.nyagram.api.exception.TelegramApiRequestException;
import pro.kaleert.nyagram.api.exception.TelegramApiValidationException;
import pro.kaleert.nyagram.api.meta.BotApiMethod;
import pro.kaleert.nyagram.api.meta.MultipartRequest;
import pro.kaleert.nyagram.api.objects.InputFile;
import pro.kaleert.nyagram.api.objects.ReplyParameters;
import pro.kaleert.nyagram.api.objects.message.Message;
import pro.kaleert.nyagram.api.objects.message.MessageEntity;
import pro.kaleert.nyagram.api.objects.message.EphemeralMessageParameters;
import pro.kaleert.nyagram.api.objects.replykeyboard.ReplyKeyboard;
import lombok.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Используйте этот метод для отправки анимации (GIF или H.264/MPEG-4 AVC видео без звука).
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
public class SendAnimation extends BotApiMethod<Message> implements MultipartRequest {

    /** Имя метода Telegram Bot API: {@code sendAnimation}. */
    public static final String PATH = "sendAnimation";

    /** Идентификатор целевого чата или имя публичного канала. */
    @JsonProperty("chat_id")
    private String chatId;

    /** Идентификатор топика в супергруппе-форуме. */
    @JsonProperty("message_thread_id")
    private Integer messageThreadId;

    /** Анимация, прикреплённая к сообщению. */
    @JsonProperty("animation")
    private InputFile animation;

    /** Продолжительность аудио или видео в секундах. */
    @JsonProperty("duration")
    private Integer duration;

    /** Ширина изображения или медиа в пикселях. */
    @JsonProperty("width")
    private Integer width;

    /** Высота изображения или медиа в пикселях. */
    @JsonProperty("height")
    private Integer height;

    /** Миниатюра медиафайла для предварительного просмотра. */
    @JsonProperty("thumbnail")
    private InputFile thumbnail;

    /** Подпись к медиафайлу. */
    @JsonProperty("caption")
    private String caption;

    /** Режим обработки разметки: HTML, Markdown или MarkdownV2. */
    @JsonProperty("parse_mode")
    private String parseMode;

    /** Сущности форматирования подписи к медиафайлу. */
    @JsonProperty("caption_entities")
    private List<MessageEntity> captionEntities;

    /** Показывать подпись над медиафайлом, если Telegram поддерживает эту возможность. */
    @JsonProperty("show_caption_above_media")
    private Boolean showCaptionAboveMedia;

    /** Скрывать медиа за спойлером до действия пользователя. */
    @JsonProperty("has_spoiler")
    private Boolean hasSpoiler;

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
     * Устаревший флаг: разрешает отправку анимации без найденного сообщения для ответа.
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
                && !ephemeralMessageParameters.callbackQueryId().isBlank())) /* BOTAPI104_EPHEMERAL_CHAT */) {
            throw new TelegramApiValidationException("ChatId is required", PATH);
        }
        if (animation == null) {
            throw new TelegramApiValidationException("Animation is required", PATH);
        }
        animation.validate();
        if (thumbnail != null) {
            thumbnail.validate();
        }
    }

    @Override
    public Map<String, InputFile> getFiles() {
        Map<String, InputFile> files = new HashMap<>();
        if (animation != null) files.put("animation", animation);
        if (thumbnail != null) files.put("thumbnail", thumbnail);
        return files;
    }

    /**
     * Отображает подпись над медиафайлом.
     * @return текущий билдер.
     */
    public SendAnimation captionAbove() {
        this.showCaptionAboveMedia = true;
        return this;
    }
    
    /**
     * Добавляет эффект к сообщению (например, огонь, конфетти).
     * @param effectId Уникальный ID эффекта.
     * @return текущий билдер.
     */
    public SendAnimation withEffect(String effectId) {
        this.messageEffectId = effectId;
        return this;
    }
    
    /**
     * Разрешить платную рассылку этого сообщения (0.1 Star за сообщение).
     * @return текущий билдер.
     * @since 1.2.0
     */
    public SendAnimation paidBroadcast() {
        this.allowPaidBroadcast = true;
        return this;
    }
}
