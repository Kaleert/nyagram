package pro.kaleert.nyagram.api.objects.widget;
import com.fasterxml.jackson.annotation.*;
import pro.kaleert.nyagram.api.meta.BotApiObject;
import java.util.List;

/**
 * Событие отправленной транзакции в виджете Telegram.
 * @param wallet Поле JSON {@code wallet} Telegram Bot API.
 * @param msgHash Поле JSON {@code msg_hash} Telegram Bot API.
 * @since 1.2.4
 */
@JsonTypeName("sent_transaction")
@JsonIgnoreProperties(ignoreUnknown = true)
public record WidgetQueryTypeSentTransaction(
    @JsonProperty("wallet") WidgetQueryWallet wallet,
    @JsonProperty("msg_hash") String msgHash
) implements WidgetQueryType {}
