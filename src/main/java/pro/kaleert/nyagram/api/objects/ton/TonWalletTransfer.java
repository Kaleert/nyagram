package pro.kaleert.nyagram.api.objects.ton;
import com.fasterxml.jackson.annotation.*;
import pro.kaleert.nyagram.api.meta.BotApiObject;
import java.util.List;

/**
 * Данные перевода через TON-кошелёк.
 * @param transactionId Поле JSON {@code transaction_id} Telegram Bot API.
 * @param peerAddress Поле JSON {@code peer_address} Telegram Bot API.
 * @param amount Количество или сумма согласно Telegram API.
 * @param comment Поле JSON {@code comment} Telegram Bot API.
 * @since 1.2.4
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record TonWalletTransfer(
    @JsonProperty("transaction_id") String transactionId,
    @JsonProperty("peer_address") String peerAddress,
    @JsonProperty("amount") String amount,
    @JsonProperty("comment") String comment
) implements BotApiObject {}
