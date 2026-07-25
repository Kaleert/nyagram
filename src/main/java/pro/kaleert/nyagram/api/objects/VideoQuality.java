package pro.kaleert.nyagram.api.objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import pro.kaleert.nyagram.api.meta.BotApiObject;

/**
 * Представляет видеофайл определенного качества.
 *
 * @since 1.1.4
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record VideoQuality(
    @JsonProperty("file_id") String fileId,
    @JsonProperty("file_unique_id") String fileUniqueId,
    @JsonProperty("width") Integer width,
    @JsonProperty("height") Integer height,
    @JsonProperty("codec") String codec,
    @JsonProperty("file_size") Long fileSize
) implements BotApiObject {}