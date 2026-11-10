package pro.kaleert.nyagram.api.objects.inlinequery.inputmessagecontent;
import com.fasterxml.jackson.annotation.*;
import pro.kaleert.nyagram.api.exception.TelegramApiValidationException;
/**
 * Содержимое сообщения, отправляемого при выборе inline-результата с общим виджетом Telegram.
 *
 * @since 1.2.4
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public record InputSharedWidgetMessageContent(
    @JsonProperty("widget_id") String widgetId,
    @JsonProperty("chat_data") String chatData,
    @JsonProperty("caption") String caption,
    @JsonProperty("parse_mode") String parseMode,
    @JsonProperty("caption_entities") java.util.List<pro.kaleert.nyagram.api.objects.message.MessageEntity> captionEntities,
    @JsonProperty("show_caption_above_media") Boolean showCaptionAboveMedia
) implements InputMessageContent {
 @Override public void validate() throws TelegramApiValidationException {

  // BOTAPI104_AUDIT_INLINE
  pro.kaleert.nyagram.api.objects.widget.WidgetLimits.utf8(chatData, 0, 1024, "chat_data", "InputSharedWidgetMessageContent"); if (widgetId==null || widgetId.isEmpty()) throw new TelegramApiValidationException("widgetId required", "InputSharedWidgetMessageContent"); }
}
