package pro.kaleert.nyagram.api.objects.richmessage;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.JsonNode;
/**
 * Блок форматированного сообщения Telegram, представляющий коллаж.
 * @param blocks Последовательность форматированных блоков.
 * @param caption Подпись.
 * @since 1.2.4
 */
@JsonTypeName("collage")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public record InputRichBlockCollage(
    @JsonProperty("blocks") java.util.List<InputRichBlock> blocks,     @JsonProperty("caption") JsonNode caption
) implements InputRichBlock {}
