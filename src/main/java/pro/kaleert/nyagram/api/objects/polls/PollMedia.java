package pro.kaleert.nyagram.api.objects.polls;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import pro.kaleert.nyagram.api.meta.BotApiObject;
import pro.kaleert.nyagram.api.objects.*;
import pro.kaleert.nyagram.api.objects.stickers.Sticker;

import java.util.List;

/**
 * Представляет медиаконтент внутри опроса.
 *
 * @since 1.2.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record PollMedia(
    @JsonProperty("type") String type,
    @JsonProperty("photo") List<PhotoSize> photo,
    @JsonProperty("video") Video video,
    @JsonProperty("document") Document document,
    @JsonProperty("audio") Audio audio,
    @JsonProperty("animation") Animation animation,
    @JsonProperty("sticker") Sticker sticker,
    @JsonProperty("location") Location location,
    @JsonProperty("venue") Venue venue,
    @JsonProperty("live_photo") LivePhoto livePhoto
) implements BotApiObject {}
