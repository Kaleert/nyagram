package pro.kaleert.nyagram.api.objects.theme;
import com.fasterxml.jackson.annotation.*;
import pro.kaleert.nyagram.api.meta.BotApiObject;
import java.util.List;

/**
 * Объект Telegram Bot API 10.4: emoji type emoji.
 * @param type Тип объекта Telegram API.
 * @param emoji Поле JSON {@code emoji} Telegram Bot API.
 * @since 1.2.4
 */
@JsonTypeName("emoji")
@JsonIgnoreProperties(ignoreUnknown = true)
public record EmojiTypeEmoji(
    @JsonProperty("type") String type,
    @JsonProperty("emoji") String emoji
) implements EmojiType {}
