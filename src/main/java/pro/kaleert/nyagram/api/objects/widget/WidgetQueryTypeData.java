package pro.kaleert.nyagram.api.objects.widget;
import com.fasterxml.jackson.annotation.*;
import pro.kaleert.nyagram.api.meta.BotApiObject;
import java.util.List;

/**
 * Запрос данных виджета Telegram.
 * @param walletCard Поле JSON {@code wallet_card} Telegram Bot API.
 * @since 1.2.4
 */
@JsonTypeName("data")
@JsonIgnoreProperties(ignoreUnknown = true)
public record WidgetQueryTypeData(
    @JsonProperty("wallet_card") pro.kaleert.nyagram.api.objects.ton.TonWalletCard walletCard
) implements WidgetQueryType {}
