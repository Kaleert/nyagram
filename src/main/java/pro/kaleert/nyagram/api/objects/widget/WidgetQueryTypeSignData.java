package pro.kaleert.nyagram.api.objects.widget;
import com.fasterxml.jackson.annotation.*;
import pro.kaleert.nyagram.api.meta.BotApiObject;
import java.util.List;

/**
 * Запрос на подпись данных виджета Telegram.
 * @param wallet Поле JSON {@code wallet} Telegram Bot API.
 * @param signData Поле JSON {@code sign_data} Telegram Bot API.
 * @since 1.2.4
 */
@JsonTypeName("sign_data")
@JsonIgnoreProperties(ignoreUnknown = true)
public record WidgetQueryTypeSignData(
    @JsonProperty("wallet") WidgetQueryWallet wallet,
    @JsonProperty("sign_data") String signData
) implements WidgetQueryType {}
