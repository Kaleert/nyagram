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
    
    public static final String PATH = "sendLivePhoto";
    
    @JsonProperty("chat_id")
    private String chatId;

    @JsonProperty("message_thread_id")
    private Integer messageThreadId;
    
    @JsonProperty("live_photo")
    private InputFile livePhoto;
    
    @JsonProperty("caption")
    private String caption;
    
    @JsonProperty("parse_mode")
    private String parseMode;

    @JsonProperty("caption_entities")
    private List<MessageEntity> captionEntities;
    
    @JsonProperty("show_caption_above_media")
    private Boolean showCaptionAboveMedia;

    @JsonProperty("has_spoiler")
    private Boolean hasSpoiler;

    @JsonProperty("disable_notification")
    private Boolean disableNotification;

    @JsonProperty("protect_content")
    private Boolean protectContent;
    
    @JsonProperty("allow_paid_broadcast")
    private Boolean allowPaidBroadcast;

    @JsonProperty("message_effect_id")
    private String messageEffectId;

    @JsonProperty("reply_parameters")
    private ReplyParameters replyParameters;

    @JsonProperty("reply_markup")
    private ReplyKeyboard replyMarkup;

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
        if (chatId == null || chatId.isEmpty()) {
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