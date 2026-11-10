package pro.kaleert.nyagram.api.objects.ton;
import com.fasterxml.jackson.annotation.*;
import pro.kaleert.nyagram.api.meta.BotApiObject;
import java.util.List;

/**
 * Модель данных TON: ton nft attribute.
 * @param traitType Поле JSON {@code trait_type} Telegram Bot API.
 * @param value Поле JSON {@code value} Telegram Bot API.
 * @since 1.2.4
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record TonNftAttribute(
    @JsonProperty("trait_type") String traitType,
    @JsonProperty("value") String value
) implements BotApiObject {}
