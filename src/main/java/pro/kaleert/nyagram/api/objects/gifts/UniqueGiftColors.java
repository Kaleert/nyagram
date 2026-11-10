package pro.kaleert.nyagram.api.objects.gifts;
import com.fasterxml.jackson.annotation.*;
import pro.kaleert.nyagram.api.meta.BotApiObject;
import java.util.List;
import com.fasterxml.jackson.databind.JsonNode;
import pro.kaleert.nyagram.api.objects.stickers.Sticker;
import pro.kaleert.nyagram.api.objects.chat.Chat;

/**
 * Цветовая палитра уникального подарка Telegram.
 * @param modelCustomEmojiId Поле JSON {@code model_custom_emoji_id} Telegram Bot API.
 * @param symbolCustomEmojiId Поле JSON {@code symbol_custom_emoji_id} Telegram Bot API.
 * @param lightThemeMainColor Поле JSON {@code light_theme_main_color} Telegram Bot API.
 * @param lightThemeOtherColors Поле JSON {@code light_theme_other_colors} Telegram Bot API.
 * @param darkThemeMainColor Поле JSON {@code dark_theme_main_color} Telegram Bot API.
 * @param darkThemeOtherColors Поле JSON {@code dark_theme_other_colors} Telegram Bot API.
 * @since 1.2.4
 */
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record UniqueGiftColors(
    @JsonProperty("model_custom_emoji_id") String modelCustomEmojiId,
    @JsonProperty("symbol_custom_emoji_id") String symbolCustomEmojiId,
    @JsonProperty("light_theme_main_color") Integer lightThemeMainColor,
    @JsonProperty("light_theme_other_colors") List<Integer> lightThemeOtherColors,
    @JsonProperty("dark_theme_main_color") Integer darkThemeMainColor,
    @JsonProperty("dark_theme_other_colors") List<Integer> darkThemeOtherColors
) implements BotApiObject {}
