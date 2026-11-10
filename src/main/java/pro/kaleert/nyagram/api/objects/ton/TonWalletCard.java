package pro.kaleert.nyagram.api.objects.ton;
import com.fasterxml.jackson.annotation.*;
import pro.kaleert.nyagram.api.meta.BotApiObject;
import java.util.List;

/**
 * Карточка TON-кошелька в Telegram.
 * @param address Адрес.
 * @param amount Количество или сумма согласно Telegram API.
 * @param stickers Поле JSON {@code stickers} Telegram Bot API.
 * @param backgroundNftAddress Поле JSON {@code background_nft_address} Telegram Bot API.
 * @since 1.2.4
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record TonWalletCard(
    @JsonProperty("address") String address,
    @JsonProperty("amount") String amount,
    @JsonProperty("stickers") List<TonWalletCardSticker> stickers,
    @JsonProperty("background_nft_address") String backgroundNftAddress
) implements BotApiObject {}
