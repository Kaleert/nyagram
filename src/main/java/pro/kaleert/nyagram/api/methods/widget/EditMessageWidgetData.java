package pro.kaleert.nyagram.api.methods.widget;
import com.fasterxml.jackson.annotation.*;
import lombok.*;
import pro.kaleert.nyagram.api.objects.widget.WidgetLimits;
import pro.kaleert.nyagram.api.exception.TelegramApiValidationException;
import pro.kaleert.nyagram.api.meta.BotApiMethodBoolean;
import pro.kaleert.nyagram.api.objects.ReplyParameters;
import pro.kaleert.nyagram.api.objects.message.MessageEntity;
import pro.kaleert.nyagram.api.objects.replykeyboard.ReplyKeyboard;
import java.util.List;
import java.nio.charset.StandardCharsets;
import java.util.zip.Deflater;

/**
 * Объект Telegram Bot API 10.4: edit message widget data.
 * @since 1.2.4
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EditMessageWidgetData extends BotApiMethodBoolean {
    
    /** Имя метода Telegram Bot API: {@code editMessageWidgetData}. */
    public static final String PATH = "editMessageWidgetData";

    /** Идентификатор чата, содержащего сообщение с виджетом. */
    @JsonProperty("chat_id") private String chatId;

    /** Идентификатор сообщения с виджетом в указанном чате. */
    @JsonProperty("message_id") private Integer messageId;

    /** Идентификатор инлайн-сообщения с виджетом. */
    @JsonProperty("inline_message_id") private String inlineMessageId;

    /** Общедоступные данные виджета, которыми заменяются данные сообщения. */
    @JsonProperty("public_data") private String publicData;

    /** Данные виджета, относящиеся к чату. */
    @JsonProperty("chat_data") private String chatData;
    @Override public String getMethod() { return PATH; }
    @Override public void validate() throws TelegramApiValidationException {
        // BOTAPI104_AUDIT_LIMITS
        WidgetLimits.utf8(publicData, 0, 1024, "public_data", PATH);
        WidgetLimits.utf8(chatData, 0, 1024, "chat_data", PATH);

        if ((inlineMessageId == null || inlineMessageId.isBlank()) && (chatId == null || messageId == null))
            throw new TelegramApiValidationException("inline_message_id or chat_id and message_id required", PATH);
    }
}
