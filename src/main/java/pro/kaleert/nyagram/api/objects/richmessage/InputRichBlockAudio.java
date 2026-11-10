package pro.kaleert.nyagram.api.objects.richmessage;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.JsonNode;
/**
 * Блок форматированного сообщения Telegram, представляющий аудио.
 * @param audio Поле JSON {@code audio} Telegram Bot API.
 * @param caption Подпись.
 * @since 1.2.4
 */
@JsonTypeName("audio")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public record InputRichBlockAudio(
    @JsonProperty("audio") JsonNode audio,     @JsonProperty("caption") JsonNode caption
) implements InputRichBlock {}
