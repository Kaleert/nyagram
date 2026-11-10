package pro.kaleert.nyagram.api.objects.theme;
import com.fasterxml.jackson.annotation.*;
import pro.kaleert.nyagram.api.meta.BotApiObject;
import java.util.List;

/**
 * Объект Telegram Bot API 10.4: emoji type custom emoji.
 * @param type Тип объекта Telegram API.
 * @param customEmojiId Поле JSON {@code custom_emoji_id} Telegram Bot API.
 * @since 1.2.4
 */
@JsonTypeName("custom_emoji")
@JsonIgnoreProperties(ignoreUnknown = true)
public record EmojiTypeCustomEmoji(
    @JsonProperty("type") String type,
    @JsonProperty("custom_emoji_id") String customEmojiId
) implements EmojiType {}
