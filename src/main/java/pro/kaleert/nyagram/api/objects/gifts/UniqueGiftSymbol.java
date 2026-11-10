package pro.kaleert.nyagram.api.objects.gifts;
import com.fasterxml.jackson.annotation.*;
import pro.kaleert.nyagram.api.meta.BotApiObject;
import java.util.List;
import com.fasterxml.jackson.databind.JsonNode;
import pro.kaleert.nyagram.api.objects.stickers.Sticker;
import pro.kaleert.nyagram.api.objects.chat.Chat;

/**
 * Символ оформления уникального подарка Telegram.
 * @param name Название объекта.
 * @param sticker Стикер.
 * @param rarityPerMille Редкость в тысячных долях.
 * @since 1.2.4
 */
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record UniqueGiftSymbol(
    @JsonProperty("name") String name,
    @JsonProperty("sticker") Sticker sticker,
    @JsonProperty("rarity_per_mille") Integer rarityPerMille
) implements BotApiObject {}
