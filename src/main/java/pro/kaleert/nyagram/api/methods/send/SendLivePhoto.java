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
 * Отправка "живых" фотографий (Live Photos).
 *
 * @since 1.2.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SendLivePhoto extends BotApiMethod<Message> implements MultipartRequest {
    
    /** Имя метода Telegram Bot API: {@code sendLivePhoto}. */
    public static final String PATH = "sendLivePhoto";
    
    /** Идентификатор целевого чата или имя публичного канала. */
    @JsonProperty("chat_id")
    private String chatId;

    /** Идентификатор топика в супергруппе-форуме. */
    @JsonProperty("message_thread_id")
    private Integer messageThreadId;
    

    /** Файл или идентификатор Live Photo, отправляемого в сообщении. */
    @JsonProperty("live_photo")
    private InputFile livePhoto;
    
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
    
    /** Разрешить платную рассылку с использованием Telegram Stars. */
    @JsonProperty("allow_paid_broadcast")
    private Boolean allowPaidBroadcast;

    /** Идентификатор визуального эффекта сообщения. */
    @JsonProperty("message_effect_id")
    private String messageEffectId;

    /** Настройки ответа на другое сообщение. */
    @JsonProperty("reply_parameters")
    private ReplyParameters replyParameters;

    /** Клавиатура или иная разметка ответа. */
    @JsonProperty("reply_markup")
    private ReplyKeyboard replyMarkup;

    /** Идентификатор бизнес-подключения для отправки от его имени. */
    @JsonProperty("business_connection_id")
    private String businessConnectionId;
    
    /** Параметры для эфемерных сообщений.
    *
    * @since 1.2.2
    */
    @JsonProperty("ephemeral_message_parameters")
    private EphemeralMessageParameters ephemeralMessageParameters;

    @Override
    public String getMethod() { return PATH; }

    @Override
    public Message deserializeResponse(String answer) throws TelegramApiRequestException {
        return deserializeResponse(answer, Message.class);
    }

    @Override
    public void validate() throws TelegramApiValidationException {
        if (((chatId == null || chatId.isEmpty()) && !(ephemeralMessageParameters != null
                && ephemeralMessageParameters.callbackQueryId() != null
                && !ephemeralMessageParameters.callbackQueryId().isBlank())) /* BOTAPI104_EPHEMERAL_CHAT */) {
            throw new TelegramApiValidationException("ChatId cannot be empty", PATH);
        }
        if (livePhoto == null) {
            throw new TelegramApiValidationException("LivePhoto cannot be null", PATH);
        }
        livePhoto.validate();
    }
    
    @Override
    public Map<String, InputFile> getFiles() {
        return livePhoto != null ? Map.of("live_photo", livePhoto) : Map.of();
    }
}