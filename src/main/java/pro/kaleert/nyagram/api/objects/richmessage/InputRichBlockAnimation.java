package pro.kaleert.nyagram.api.objects.richmessage;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.JsonNode;
/**
 * Блок форматированного сообщения Telegram, представляющий анимацию.
 * @param animation Поле JSON {@code animation} Telegram Bot API.
 * @param caption Подпись.
 * @since 1.2.4
 */
@JsonTypeName("animation")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public record InputRichBlockAnimation(
    @JsonProperty("animation") JsonNode animation,     @JsonProperty("caption") JsonNode caption
) implements InputRichBlock {}
