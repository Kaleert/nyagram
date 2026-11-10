package pro.kaleert.nyagram.api.objects.richmessage;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.JsonNode;
/**
 * Блок форматированного сообщения Telegram, представляющий кнопки.
 * @param buttons Поле JSON {@code buttons} Telegram Bot API.
 * @param align Поле JSON {@code align} Telegram Bot API.
 * @since 1.2.4
 */
@JsonTypeName("buttons")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public record InputRichBlockButtons(
    @JsonProperty("buttons") java.util.List<JsonNode> buttons,     @JsonProperty("align") String align
) implements InputRichBlock {}
