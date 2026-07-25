package pro.kaleert.nyagram.api.methods.updatingmessages;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import pro.kaleert.nyagram.api.exception.TelegramApiRequestException;
import pro.kaleert.nyagram.api.exception.TelegramApiValidationException;
import pro.kaleert.nyagram.api.meta.BotApiMethod;
import pro.kaleert.nyagram.api.meta.MultipartRequest;
import pro.kaleert.nyagram.api.objects.InputFile;
import pro.kaleert.nyagram.api.objects.media.InputMedia;
import pro.kaleert.nyagram.api.objects.replykeyboard.InlineKeyboardMarkup;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Используйте этот метод для редактирования медиафайла в сообщении (замена фото, видео, документа).
 *
 * @since 1.1.5
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EditMessageMedia extends BotApiMethod<Serializable> implements MultipartRequest {

    public static final String PATH = "editMessageMedia";

    @JsonProperty("chat_id")
    private String chatId;

    @JsonProperty("message_id")
    private Integer messageId;

    @JsonProperty("inline_message_id")
    private String inlineMessageId;

    @JsonProperty("media")
    private InputMedia media;

    @JsonProperty("reply_markup")
    private InlineKeyboardMarkup replyMarkup;

    @JsonProperty("business_connection_id")
    private String businessConnectionId;

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
        if (media == null) {
            throw new TelegramApiValidationException("Media is required", PATH);
        }
        media.validate();
    }

    @Override
    public Map<String, InputFile> getFiles() {
        Map<String, InputFile> files = new HashMap<>();
        
        if (media != null) {
            InputFile mainFile = media.getMediaFile();
            if (mainFile != null && mainFile.isNew()) {
                String attachName = "file_" + UUID.randomUUID();
                files.put(attachName, mainFile);
                this.media = this.media.withMedia("attach://" + attachName);
            }

            InputFile thumbFile = this.media.getThumbnail();
            if (thumbFile != null && thumbFile.isNew()) {
                String thumbName = "thumb_" + UUID.randomUUID();
                files.put(thumbName, thumbFile);
                if (this.media instanceof pro.kaleert.nyagram.api.objects.media.InputMediaVideo vid) {
                     this.media = vid.withThumbnail(new InputFile("attach://" + thumbName));
                }
            }
        }
        return files;
    }
}