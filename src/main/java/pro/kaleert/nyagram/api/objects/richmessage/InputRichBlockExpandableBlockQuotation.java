package pro.kaleert.nyagram.api.objects.richmessage;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.JsonNode;
/**
 * Блок форматированного сообщения Telegram, представляющий раскрываемую цитату.
 * @param text Текстовое содержимое.
 * @param credit Поле JSON {@code credit} Telegram Bot API.
 * @since 1.2.4
 */
@JsonTypeName("expandable_blockquote")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public record InputRichBlockExpandableBlockQuotation(
    @JsonProperty("text") JsonNode text,     @JsonProperty("credit") JsonNode credit
) implements InputRichBlock {}
