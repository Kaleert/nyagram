package pro.kaleert.nyagram.api.objects.richmessage;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.JsonNode;
/**
 * Блок форматированного сообщения Telegram, представляющий выносную цитату.
 * @param text Текстовое содержимое.
 * @param credit Поле JSON {@code credit} Telegram Bot API.
 * @since 1.2.4
 */
@JsonTypeName("pullquote")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public record InputRichBlockPullQuotation(
    @JsonProperty("text") JsonNode text,     @JsonProperty("credit") JsonNode credit
) implements InputRichBlock {}
