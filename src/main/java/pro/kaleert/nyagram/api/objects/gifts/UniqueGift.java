package pro.kaleert.nyagram.api.objects.gifts;
import com.fasterxml.jackson.annotation.*;
import pro.kaleert.nyagram.api.meta.BotApiObject;
import java.util.List;
import com.fasterxml.jackson.databind.JsonNode;
import pro.kaleert.nyagram.api.objects.stickers.Sticker;
import pro.kaleert.nyagram.api.objects.chat.Chat;

/**
 * Сведения об уникальном подарке Telegram.
 * @param giftId Идентификатор подарка.
 * @param baseName Поле JSON {@code base_name} Telegram Bot API.
 * @param name Название объекта.
 * @param number Поле JSON {@code number} Telegram Bot API.
 * @param model Поле JSON {@code model} Telegram Bot API.
 * @param symbol Поле JSON {@code symbol} Telegram Bot API.
 * @param backdrop Поле JSON {@code backdrop} Telegram Bot API.
 * @param isPremium Поле JSON {@code is_premium} Telegram Bot API.
 * @param isBurned Поле JSON {@code is_burned} Telegram Bot API.
 * @param isFromBlockchain Поле JSON {@code is_from_blockchain} Telegram Bot API.
 * @param colors Цвета оформления.
 * @param publisherChat Поле JSON {@code publisher_chat} Telegram Bot API.
 * @since 1.2.4
 */
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record UniqueGift(
    @JsonProperty("gift_id") String giftId,
    @JsonProperty("base_name") String baseName,
    @JsonProperty("name") String name,
    @JsonProperty("number") Integer number,
    @JsonProperty("model") UniqueGiftModel model,
    @JsonProperty("symbol") UniqueGiftSymbol symbol,
    @JsonProperty("backdrop") UniqueGiftBackdrop backdrop,
    @JsonProperty("is_premium") Boolean isPremium,
    @JsonProperty("is_burned") Boolean isBurned,
    @JsonProperty("is_from_blockchain") Boolean isFromBlockchain,
    @JsonProperty("colors") UniqueGiftColors colors,
    @JsonProperty("publisher_chat") Chat publisherChat
) implements BotApiObject {}
