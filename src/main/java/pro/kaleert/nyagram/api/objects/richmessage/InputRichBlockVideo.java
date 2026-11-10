package pro.kaleert.nyagram.api.objects.richmessage;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.JsonNode;
/**
 * Блок форматированного сообщения Telegram, представляющий видео.
 * @param video Поле JSON {@code video} Telegram Bot API.
 * @param caption Подпись.
 * @since 1.2.4
 */
@JsonTypeName("video")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public record InputRichBlockVideo(
    @JsonProperty("video") JsonNode video,     @JsonProperty("caption") JsonNode caption
) implements InputRichBlock {}
