package pro.kaleert.nyagram.api.objects.theme;
import com.fasterxml.jackson.annotation.*;
import pro.kaleert.nyagram.api.meta.BotApiObject;
import java.util.List;

/**
 * Вариант темы оформления чата Telegram: unique gift.
 * @param type Тип объекта Telegram API.
 * @param gift Поле JSON {@code gift} Telegram Bot API.
 * @since 1.2.4
 */
@JsonTypeName("unique_gift")
@JsonIgnoreProperties(ignoreUnknown = true)
public record ChatShareThemeUniqueGift(
    @JsonProperty("type") String type,
    @JsonProperty("gift") pro.kaleert.nyagram.api.objects.gifts.UniqueGift gift
) implements ChatShareTheme {}
