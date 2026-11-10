package pro.kaleert.nyagram.api.objects.richmessage;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.JsonNode;
/**
 * Блок форматированного сообщения Telegram, представляющий предформатированный текст.
 * @param text Текстовое содержимое.
 * @param language Поле JSON {@code language} Telegram Bot API.
 * @since 1.2.4
 */
@JsonTypeName("pre")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public record InputRichBlockPreformatted(
    @JsonProperty("text") JsonNode text,     @JsonProperty("language") String language
) implements InputRichBlock {}
