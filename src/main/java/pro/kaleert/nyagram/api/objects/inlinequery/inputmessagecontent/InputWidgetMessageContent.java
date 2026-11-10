package pro.kaleert.nyagram.api.objects.inlinequery.inputmessagecontent;
import com.fasterxml.jackson.annotation.*;
import pro.kaleert.nyagram.api.exception.TelegramApiValidationException;
/**
 * Содержимое сообщения, отправляемого при выборе inline-результата с виджетом Telegram.
 *
 * @since 1.2.4
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public record InputWidgetMessageContent(
    @JsonProperty("html") String html,
    @JsonProperty("width") Integer width,
    @JsonProperty("height") Integer height,
    @JsonProperty("public_data") String publicData,
    @JsonProperty("chat_data") String chatData,
    @JsonProperty("caption") String caption,
    @JsonProperty("parse_mode") String parseMode,
    @JsonProperty("caption_entities") java.util.List<pro.kaleert.nyagram.api.objects.message.MessageEntity> captionEntities,
    @JsonProperty("show_caption_above_media") Boolean showCaptionAboveMedia,
    @JsonProperty("allow_queries") Boolean allowQueries,
    @JsonProperty("is_shared") Boolean isShared
) implements InputMessageContent {
 @Override public void validate() throws TelegramApiValidationException {

  // BOTAPI104_AUDIT_INLINE
  pro.kaleert.nyagram.api.objects.widget.WidgetLimits.html(html, "InputWidgetMessageContent");
  pro.kaleert.nyagram.api.objects.widget.WidgetLimits.utf8(publicData, 0, 1024, "public_data", "InputWidgetMessageContent");
  pro.kaleert.nyagram.api.objects.widget.WidgetLimits.utf8(chatData, 0, 1024, "chat_data", "InputWidgetMessageContent"); if (html==null || html.isEmpty()) throw new TelegramApiValidationException("html required", "InputWidgetMessageContent"); }
}
