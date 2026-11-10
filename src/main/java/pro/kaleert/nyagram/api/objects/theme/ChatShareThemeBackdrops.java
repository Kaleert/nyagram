package pro.kaleert.nyagram.api.objects.theme;
import com.fasterxml.jackson.annotation.*;
import pro.kaleert.nyagram.api.meta.BotApiObject;
import java.util.List;

/**
 * Вариант темы оформления чата Telegram: backdrops.
 * @param backdrops Поле JSON {@code backdrops} Telegram Bot API.
 * @since 1.2.4
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record ChatShareThemeBackdrops(
    @JsonProperty("backdrops") List<ChatShareThemeBackdrop> backdrops
) implements BotApiObject {}
