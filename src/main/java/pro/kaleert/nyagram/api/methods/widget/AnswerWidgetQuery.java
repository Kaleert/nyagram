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
 * Объект Telegram Bot API 10.4: answer widget query.
 * @since 1.2.4
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AnswerWidgetQuery extends BotApiMethodBoolean {
    
    /** Имя метода Telegram Bot API: {@code answerWidgetQuery}. */
    public static final String PATH = "answerWidgetQuery";

    /** Идентификатор запроса виджета, на который отправляется ответ. */
    @JsonProperty("widget_query_id") private String widgetQueryId;

    /** Данные, возвращаемые в ответ на запрос виджета. */
    @JsonProperty("data") private String data;
    @Override public String getMethod() { return PATH; }
    @Override public void validate() throws TelegramApiValidationException {
        // BOTAPI104_AUDIT_LIMITS
        WidgetLimits.utf8(data, 0, 1024, "data", PATH);

        if (widgetQueryId == null || widgetQueryId.isEmpty()) throw new TelegramApiValidationException("widgetQueryId required", PATH);
    }
}
