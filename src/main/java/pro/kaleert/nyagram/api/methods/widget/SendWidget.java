package pro.kaleert.nyagram.api.methods.widget;
import com.fasterxml.jackson.annotation.*;
import lombok.*;
import pro.kaleert.nyagram.api.objects.widget.WidgetLimits;
import pro.kaleert.nyagram.api.exception.TelegramApiValidationException;
import pro.kaleert.nyagram.api.meta.BotApiMethod;
import pro.kaleert.nyagram.api.exception.TelegramApiRequestException;
import pro.kaleert.nyagram.api.objects.message.Message;
import pro.kaleert.nyagram.api.objects.ReplyParameters;
import pro.kaleert.nyagram.api.objects.message.MessageEntity;
import pro.kaleert.nyagram.api.objects.replykeyboard.ReplyKeyboard;
import java.util.List;
import java.nio.charset.StandardCharsets;
import java.util.zip.Deflater;

/**
 * Объект Telegram Bot API 10.4: send widget.
 * @since 1.2.4
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SendWidget extends BotApiMethod<Message> {
    public static final String PATH = "sendWidget";

    /** Идентификатор чата, в который отправляется виджет. */
    @JsonProperty("chat_id") private String chatId;

    /** Идентификатор темы форума, куда отправляется виджет. */
    @JsonProperty("message_thread_id") private Integer messageThreadId;

    /** HTML-содержимое виджета. */
    @JsonProperty("html") private String html;
    @JsonProperty("width") private Integer width;

    /** Высота отображаемого виджета. */
    @JsonProperty("height") private Integer height;

    /** Общедоступные данные виджета, передаваемые получателям. */
    @JsonProperty("public_data") private String publicData;

    /** Данные виджета, относящиеся к чату получателя. */
    @JsonProperty("chat_data") private String chatData;

    /** Подпись к отправляемому виджету. */
    @JsonProperty("caption") private String caption;

    /** Режим разбора форматирования подписи виджета. */
    @JsonProperty("parse_mode") private String parseMode;

    /** Сущности форматирования подписи виджета. */
    @JsonProperty("caption_entities") private List<MessageEntity> captionEntities;
    @JsonProperty("show_caption_above_media") private Boolean showCaptionAboveMedia;

    /** Разрешает пользователям отправлять запросы из виджета боту. */
    @JsonProperty("allow_queries") private Boolean allowQueries;

    /** Признак общего виджета, доступного для совместного использования. */
    @JsonProperty("is_shared") private Boolean isShared;

    /** Отправляет виджет без звукового уведомления. */
    @JsonProperty("disable_notification") private Boolean disableNotification;

    /** Запрещает пересылку и сохранение содержимого сообщения. */
    @JsonProperty("protect_content") private Boolean protectContent;

    /** Разрешает платную массовую отправку сверх обычных лимитов Telegram. */
    @JsonProperty("allow_paid_broadcast") private Boolean allowPaidBroadcast;

    /** Идентификатор визуального эффекта отправляемого сообщения. */
    @JsonProperty("message_effect_id") private String messageEffectId;
    @JsonProperty("reply_parameters") private ReplyParameters replyParameters;
    @JsonProperty("reply_markup") private ReplyKeyboard replyMarkup;
    @Override public String getMethod() { return PATH; }
    @Override public Message deserializeResponse(String answer) throws TelegramApiRequestException { return deserializeResponse(answer, Message.class); }
    @Override public void validate() throws TelegramApiValidationException {
        // BOTAPI104_AUDIT_LIMITS
        WidgetLimits.utf8(publicData, 0, 1024, "public_data", PATH);
        WidgetLimits.utf8(chatData, 0, 1024, "chat_data", PATH);

        if (chatId == null || chatId.isEmpty()) throw new TelegramApiValidationException("chatId required", PATH);
        if (html == null || html.isEmpty()) throw new TelegramApiValidationException("html required", PATH);
        if (html.isEmpty()) throw new TelegramApiValidationException("html required", PATH);
        byte[] bytes = html.getBytes(StandardCharsets.UTF_8);
        if (bytes.length > 32768) throw new TelegramApiValidationException("html too large (32768 bytes)", PATH);
        Deflater deflater = new Deflater(9);
        deflater.setInput(bytes); deflater.finish();
        byte[] compressed = new byte[4097]; int size = deflater.deflate(compressed);
        boolean finished = deflater.finished(); deflater.end();
        if (!finished || size > 4096) throw new TelegramApiValidationException("html too large after zlib compression (4096 bytes)", PATH);
    }
}
