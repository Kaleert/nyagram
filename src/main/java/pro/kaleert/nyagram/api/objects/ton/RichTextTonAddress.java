package pro.kaleert.nyagram.api.objects.ton;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.JsonNode;
import pro.kaleert.nyagram.api.meta.BotApiObject;
/**
 * Объект Telegram Bot API 10.4: rich text ton address.
 * @param type Тип объекта Telegram API.
 * @param text Текстовое содержимое.
 * @param address Адрес.
 * @since 1.2.4
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record RichTextTonAddress(
    @JsonProperty("type") String type,
    @JsonProperty("text") JsonNode text,
    @JsonProperty("address") String address
) implements BotApiObject {

    /**
     * Преобразует JSON-представление текста TON-адреса в форматированное значение.
     *
     * @return форматированный текст или {@code null}, если текст отсутствует
     */
    @com.fasterxml.jackson.annotation.JsonIgnore
    public pro.kaleert.nyagram.api.objects.richtext.RichTextValue decodedText() {
        return text == null ? null : pro.kaleert.nyagram.api.objects.richtext.RichTextValue.from(text);
    }
}
