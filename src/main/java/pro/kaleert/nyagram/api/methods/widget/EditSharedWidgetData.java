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
 * Объект Telegram Bot API 10.4: edit shared widget data.
 * @since 1.2.4
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EditSharedWidgetData extends BotApiMethodBoolean {
    public static final String PATH = "editSharedWidgetData";

    /** Идентификатор общего виджета, данные которого изменяются. */
    @JsonProperty("widget_id") private String widgetId;

    /** Общедоступные данные общего виджета, которые требуется обновить. */
    @JsonProperty("public_data") private String publicData;

    /** Время кэширования обновлённых данных виджета в секундах. */
    @JsonProperty("cache_time") private Integer cacheTime;
    @Override public String getMethod() { return PATH; }
    @Override public void validate() throws TelegramApiValidationException {
        // BOTAPI104_AUDIT_LIMITS
        WidgetLimits.utf8(publicData, 0, 1024, "public_data", PATH);
        if (cacheTime != null && (cacheTime < 5 || cacheTime > 3600)) throw new TelegramApiValidationException("cache_time must be 5..3600 seconds", PATH);

        if (widgetId == null || widgetId.isEmpty()) throw new TelegramApiValidationException("widgetId required", PATH);
        if (publicData == null) throw new TelegramApiValidationException("publicData required", PATH);
    }
}
