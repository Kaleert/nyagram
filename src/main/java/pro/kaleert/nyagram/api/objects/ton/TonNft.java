package pro.kaleert.nyagram.api.objects.ton;
import com.fasterxml.jackson.annotation.*;
import pro.kaleert.nyagram.api.meta.BotApiObject;
import java.util.List;

/**
 * Сведения о NFT в сети TON.
 * @param address Адрес.
 * @param collectionAddress Поле JSON {@code collection_address} Telegram Bot API.
 * @param index Поле JSON {@code index} Telegram Bot API.
 * @param name Название объекта.
 * @param description Поле JSON {@code description} Telegram Bot API.
 * @param attributes Поле JSON {@code attributes} Telegram Bot API.
 * @param gift Поле JSON {@code gift} Telegram Bot API.
 * @since 1.2.4
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record TonNft(
    @JsonProperty("address") String address,
    @JsonProperty("collection_address") String collectionAddress,
    @JsonProperty("index") String index,
    @JsonProperty("name") String name,
    @JsonProperty("description") String description,
    @JsonProperty("attributes") List<TonNftAttribute> attributes,
    @JsonProperty("gift") pro.kaleert.nyagram.api.objects.gifts.UniqueGift gift
) implements BotApiObject {}
