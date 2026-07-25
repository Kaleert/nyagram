package pro.kaleert.nyagram.api.objects.gifts;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import pro.kaleert.nyagram.api.meta.BotApiObject;

/**
 * Содержит информацию о подарке, отправленном в чат.
 * 
 * @since 1.2.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record GiftInfo(
    @JsonProperty("gift") Gift gift,
    @JsonProperty("star_count") Integer starCount,
    @JsonProperty("upgrade_star_count") Integer upgradeStarCount
) implements BotApiObject {}