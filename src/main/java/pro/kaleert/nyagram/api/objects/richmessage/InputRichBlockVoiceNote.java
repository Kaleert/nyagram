package pro.kaleert.nyagram.api.objects.richmessage;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.JsonNode;
/**
 * Блок форматированного сообщения Telegram, представляющий голосовое сообщение.
 * @param voiceNote Поле JSON {@code voice_note} Telegram Bot API.
 * @param caption Подпись.
 * @since 1.2.4
 */
@JsonTypeName("voice_note")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public record InputRichBlockVoiceNote(
    @JsonProperty("voice_note") JsonNode voiceNote,     @JsonProperty("caption") JsonNode caption
) implements InputRichBlock {}
