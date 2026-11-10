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
 * Объект Telegram Bot API 10.4: send shared widget.
 * @since 1.2.4
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SendSharedWidget extends BotApiMethod<Message> {
    public static final String PATH = "sendSharedWidget";

    /** Идентификатор чата, в который отправляется общий виджет. */
    @JsonProperty("chat_id") private String chatId;

    /** Идентификатор темы форума, куда отправляется виджет. */
    @JsonProperty("message_thread_id") private Integer messageThreadId;

    /** Идентификатор общего виджета для отправки. */
    @JsonProperty("widget_id") private String widgetId;

    /** Данные общего виджета, связанные с чатом получателя. */
    @JsonProperty("chat_data") private String chatData;

    /** Подпись к отправляемому общему виджету. */
    @JsonProperty("caption") private String caption;

    /** Режим разбора форматирования подписи виджета. */
    @JsonProperty("parse_mode") private String parseMode;

    /** Сущности форматирования подписи виджета. */
    @JsonProperty("caption_entities") private List<MessageEntity> captionEntities;

    /** Определяет расположение подписи над медиа. */
    @JsonProperty("show_caption_above_media") private Boolean showCaptionAboveMedia;

    /** Отправляет виджет без звукового уведомления. */
    @JsonProperty("disable_notification") private Boolean disableNotification;

    /** Запрещает пересылку и сохранение содержимого сообщения. */
    @JsonProperty("protect_content") private Boolean protectContent;

    /** Разрешает платную массовую отправку сверх обычных лимитов Telegram. */
    @JsonProperty("allow_paid_broadcast") private Boolean allowPaidBroadcast;

    /** Идентификатор визуального эффекта отправляемого сообщения. */
    @JsonProperty("message_effect_id") private String messageEffectId;

    /** Параметры ответа на другое сообщение. */
    @JsonProperty("reply_parameters") private ReplyParameters replyParameters;

    /** Клавиатура, прикрепляемая к сообщению с общим виджетом. */
    @JsonProperty("reply_markup") private ReplyKeyboard replyMarkup;
    @Override public String getMethod() { return PATH; }
    @Override public Message deserializeResponse(String answer) throws TelegramApiRequestException { return deserializeResponse(answer, Message.class); }
    @Override public void validate() throws TelegramApiValidationException {
        // BOTAPI104_AUDIT_LIMITS
        WidgetLimits.utf8(chatData, 0, 1024, "chat_data", PATH);

        if (chatId == null || chatId.isEmpty()) throw new TelegramApiValidationException("chatId required", PATH);
        if (widgetId == null || widgetId.isEmpty()) throw new TelegramApiValidationException("widgetId required", PATH);
    }
}
