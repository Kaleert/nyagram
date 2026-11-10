package pro.kaleert.nyagram.api.objects.richmessage;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.JsonNode;
/**
 * Блок форматированного сообщения Telegram, представляющий абзац.
 * @param text Текстовое содержимое.
 * @since 1.2.4
 */
@JsonTypeName("paragraph")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public record InputRichBlockParagraph(
    @JsonProperty("text") JsonNode text
) implements InputRichBlock {}
