package pro.kaleert.nyagram.api.objects.richmessage;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.JsonNode;
/**
 * Блок форматированного сообщения Telegram, представляющий таблицу.
 * @param cells Поле JSON {@code cells} Telegram Bot API.
 * @param isBordered Поле JSON {@code is_bordered} Telegram Bot API.
 * @param isStriped Поле JSON {@code is_striped} Telegram Bot API.
 * @param isCompact Поле JSON {@code is_compact} Telegram Bot API.
 * @param caption Подпись.
 * @since 1.2.4
 */
@JsonTypeName("table")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public record InputRichBlockTable(
    @JsonProperty("cells") java.util.List<java.util.List<JsonNode>> cells,     @JsonProperty("is_bordered") Boolean isBordered,     @JsonProperty("is_striped") Boolean isStriped,     @JsonProperty("is_compact") Boolean isCompact,     @JsonProperty("caption") JsonNode caption
) implements InputRichBlock {}
