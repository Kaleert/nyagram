package pro.kaleert.nyagram.api.objects.richmessage;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.JsonNode;
/**
 * Блок форматированного сообщения Telegram, представляющий нижний колонтитул.
 * @param text Текстовое содержимое.
 * @since 1.2.4
 */
@JsonTypeName("footer")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public record InputRichBlockFooter(
    @JsonProperty("text") JsonNode text
) implements InputRichBlock {}
