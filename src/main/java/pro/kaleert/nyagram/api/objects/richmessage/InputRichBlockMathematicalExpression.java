package pro.kaleert.nyagram.api.objects.richmessage;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.JsonNode;
/**
 * Блок форматированного сообщения Telegram, представляющий математическое выражение.
 * @param expression Поле JSON {@code expression} Telegram Bot API.
 * @since 1.2.4
 */
@JsonTypeName("mathematical_expression")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public record InputRichBlockMathematicalExpression(
    @JsonProperty("expression") String expression
) implements InputRichBlock {}
