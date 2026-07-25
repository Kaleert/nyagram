package pro.kaleert.nyagram.api.objects.gifts;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import pro.kaleert.nyagram.api.meta.BotApiObject;
import pro.kaleert.nyagram.api.objects.stickers.Sticker;

/**
 * Представляет подарок (Gift), который может быть отправлен пользователю.
 *
 * @since 1.1.4
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record Gift(
    @JsonProperty("id") String id,
    @JsonProperty("sticker") Sticker sticker,
    @JsonProperty("star_count") Integer starCount,
    @JsonProperty("total_count") Integer totalCount,
    @JsonProperty("remaining_count") Integer remainingCount,
    @JsonProperty("upgrade_star_count") Integer upgradeStarCount
) implements BotApiObject {}