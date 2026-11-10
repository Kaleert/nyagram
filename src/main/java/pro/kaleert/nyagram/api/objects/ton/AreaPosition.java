package pro.kaleert.nyagram.api.objects.ton;
import com.fasterxml.jackson.annotation.*;
import pro.kaleert.nyagram.api.meta.BotApiObject;
import java.util.List;

/**
 * Объект Telegram Bot API 10.4: area position.
 * @param xPercentage Поле JSON {@code x_percentage} Telegram Bot API.
 * @param yPercentage Поле JSON {@code y_percentage} Telegram Bot API.
 * @param widthPercentage Поле JSON {@code width_percentage} Telegram Bot API.
 * @param heightPercentage Поле JSON {@code height_percentage} Telegram Bot API.
 * @param rotationAngle Поле JSON {@code rotation_angle} Telegram Bot API.
 * @param cornerRadiusPercentage Поле JSON {@code corner_radius_percentage} Telegram Bot API.
 * @since 1.2.4
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record AreaPosition(
    @JsonProperty("x_percentage") Double xPercentage,
    @JsonProperty("y_percentage") Double yPercentage,
    @JsonProperty("width_percentage") Double widthPercentage,
    @JsonProperty("height_percentage") Double heightPercentage,
    @JsonProperty("rotation_angle") Double rotationAngle,
    @JsonProperty("corner_radius_percentage") Double cornerRadiusPercentage
) implements BotApiObject {}
