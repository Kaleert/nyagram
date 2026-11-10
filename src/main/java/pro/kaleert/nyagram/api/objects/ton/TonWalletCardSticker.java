package pro.kaleert.nyagram.api.objects.ton;
import com.fasterxml.jackson.annotation.*;
import pro.kaleert.nyagram.api.meta.BotApiObject;
import java.util.List;

/**
 * Модель данных TON: ton wallet card sticker.
 * @param position Поле JSON {@code position} Telegram Bot API.
 * @param nft Поле JSON {@code nft} Telegram Bot API.
 * @since 1.2.4
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record TonWalletCardSticker(
    @JsonProperty("position") AreaPosition position,
    @JsonProperty("nft") TonNft nft
) implements BotApiObject {}
