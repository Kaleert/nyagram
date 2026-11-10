package pro.kaleert.nyagram.api.objects.richmessage;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.JsonNode;
/**
 * Блок форматированного сообщения Telegram, представляющий раскрываемые детали.
 * @param summary Поле JSON {@code summary} Telegram Bot API.
 * @param blocks Последовательность форматированных блоков.
 * @param isOpen Поле JSON {@code is_open} Telegram Bot API.
 * @since 1.2.4
 */
@JsonTypeName("details")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public record InputRichBlockDetails(
    @JsonProperty("summary") JsonNode summary,     @JsonProperty("blocks") java.util.List<InputRichBlock> blocks,     @JsonProperty("is_open") Boolean isOpen
) implements InputRichBlock {}
