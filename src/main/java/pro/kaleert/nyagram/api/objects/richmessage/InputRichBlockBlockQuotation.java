package pro.kaleert.nyagram.api.objects.richmessage;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.JsonNode;
/**
 * Блок форматированного сообщения Telegram, представляющий блочную цитату.
 * @param blocks Последовательность форматированных блоков.
 * @param credit Поле JSON {@code credit} Telegram Bot API.
 * @since 1.2.4
 */
@JsonTypeName("blockquote")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public record InputRichBlockBlockQuotation(
    @JsonProperty("blocks") java.util.List<InputRichBlock> blocks,     @JsonProperty("credit") JsonNode credit
) implements InputRichBlock {}
