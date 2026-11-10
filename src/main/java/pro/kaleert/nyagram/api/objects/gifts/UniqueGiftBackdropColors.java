package pro.kaleert.nyagram.api.objects.gifts;
import com.fasterxml.jackson.annotation.*;
import pro.kaleert.nyagram.api.meta.BotApiObject;
import java.util.List;
import com.fasterxml.jackson.databind.JsonNode;
import pro.kaleert.nyagram.api.objects.stickers.Sticker;
import pro.kaleert.nyagram.api.objects.chat.Chat;

/**
 * Цвета фона оформления уникального подарка Telegram.
 * @param centerColor Поле JSON {@code center_color} Telegram Bot API.
 * @param edgeColor Поле JSON {@code edge_color} Telegram Bot API.
 * @param symbolColor Поле JSON {@code symbol_color} Telegram Bot API.
 * @param textColor Поле JSON {@code text_color} Telegram Bot API.
 * @since 1.2.4
 */
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record UniqueGiftBackdropColors(
    @JsonProperty("center_color") Integer centerColor,
    @JsonProperty("edge_color") Integer edgeColor,
    @JsonProperty("symbol_color") Integer symbolColor,
    @JsonProperty("text_color") Integer textColor
) implements BotApiObject {}
