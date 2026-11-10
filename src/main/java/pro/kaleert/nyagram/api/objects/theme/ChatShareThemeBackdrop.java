package pro.kaleert.nyagram.api.objects.theme;
import com.fasterxml.jackson.annotation.*;
import pro.kaleert.nyagram.api.meta.BotApiObject;
import java.util.List;

/**
 * Вариант темы оформления чата Telegram: backdrop.
 * @param id Идентификатор объекта.
 * @param name Название объекта.
 * @param colors Цвета оформления.
 * @since 1.2.4
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record ChatShareThemeBackdrop(
    @JsonProperty("id") Integer id,
    @JsonProperty("name") String name,
    @JsonProperty("colors") pro.kaleert.nyagram.api.objects.gifts.UniqueGiftBackdropColors colors
) implements BotApiObject {}
