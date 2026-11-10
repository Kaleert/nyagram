package pro.kaleert.nyagram.api.objects.richmessage;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.JsonNode;
/**
 * Блок форматированного сообщения Telegram, представляющий якорь.
 * @param name Название объекта.
 * @since 1.2.4
 */
@JsonTypeName("anchor")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public record InputRichBlockAnchor(
    @JsonProperty("name") String name
) implements InputRichBlock {}
