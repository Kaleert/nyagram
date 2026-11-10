package pro.kaleert.nyagram.api.objects.richmessage;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.JsonNode;
/**
 * Блок форматированного сообщения Telegram, представляющий список.
 * @param items Поле JSON {@code items} Telegram Bot API.
 * @since 1.2.4
 */
@JsonTypeName("list")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public record InputRichBlockList(
    @JsonProperty("items") java.util.List<InputRichBlockListItem> items
) implements InputRichBlock {}
