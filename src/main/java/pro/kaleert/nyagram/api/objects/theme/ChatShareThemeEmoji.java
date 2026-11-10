package pro.kaleert.nyagram.api.objects.theme;
import com.fasterxml.jackson.annotation.*;
import pro.kaleert.nyagram.api.meta.BotApiObject;
import java.util.List;

/**
 * Вариант темы оформления чата Telegram: emoji.
 * @param type Тип объекта Telegram API.
 * @param emojis Поле JSON {@code emojis} Telegram Bot API.
 * @param backgroundBackdropId Поле JSON {@code background_backdrop_id} Telegram Bot API.
 * @since 1.2.4
 */
@JsonTypeName("emoji")
@JsonIgnoreProperties(ignoreUnknown = true)
public record ChatShareThemeEmoji(
    @JsonProperty("type") String type,
    @JsonProperty("emojis") List<EmojiType> emojis,
    @JsonProperty("background_backdrop_id") Integer backgroundBackdropId
) implements ChatShareTheme {}
