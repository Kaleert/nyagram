package pro.kaleert.nyagram.api.objects.richmessage;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.JsonNode;
/**
 * Блок форматированного сообщения Telegram, представляющий заголовок.
 * @param text Текстовое содержимое.
 * @param size Поле JSON {@code size} Telegram Bot API.
 * @since 1.2.4
 */
@JsonTypeName("heading")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public record InputRichBlockHeading(
    @JsonProperty("text") JsonNode text,     @JsonProperty("size") Integer size
) implements InputRichBlock {}
