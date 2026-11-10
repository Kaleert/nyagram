package pro.kaleert.nyagram.api.objects.richmessage;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.JsonNode;
/**
 * Блок форматированного сообщения Telegram, представляющий карту.
 * @param location Поле JSON {@code location} Telegram Bot API.
 * @param zoom Поле JSON {@code zoom} Telegram Bot API.
 * @param width Ширина.
 * @param height Высота.
 * @param caption Подпись.
 * @since 1.2.4
 */
@JsonTypeName("map")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public record InputRichBlockMap(
    @JsonProperty("location") JsonNode location,     @JsonProperty("zoom") Integer zoom,     @JsonProperty("width") Integer width,     @JsonProperty("height") Integer height,     @JsonProperty("caption") JsonNode caption
) implements InputRichBlock {}
