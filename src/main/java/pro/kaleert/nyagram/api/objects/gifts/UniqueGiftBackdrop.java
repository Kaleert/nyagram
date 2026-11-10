package pro.kaleert.nyagram.api.objects.gifts;
import com.fasterxml.jackson.annotation.*;
import pro.kaleert.nyagram.api.meta.BotApiObject;
import java.util.List;
import com.fasterxml.jackson.databind.JsonNode;
import pro.kaleert.nyagram.api.objects.stickers.Sticker;
import pro.kaleert.nyagram.api.objects.chat.Chat;

/**
 * Фон оформления уникального подарка Telegram.
 * @param name Название объекта.
 * @param colors Цвета оформления.
 * @param rarityPerMille Редкость в тысячных долях.
 * @since 1.2.4
 */
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record UniqueGiftBackdrop(
    @JsonProperty("name") String name,
    @JsonProperty("colors") UniqueGiftBackdropColors colors,
    @JsonProperty("rarity_per_mille") Integer rarityPerMille
) implements BotApiObject {}
