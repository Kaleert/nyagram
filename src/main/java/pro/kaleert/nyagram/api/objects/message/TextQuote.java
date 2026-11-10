package pro.kaleert.nyagram.api.objects.message;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import pro.kaleert.nyagram.api.meta.BotApiObject;
import java.util.List;

/**
 * Содержит информацию о цитируемой части сообщения, на которое дается ответ.
 *
 * @param text Текст цитаты.
 * @param entities Специальные сущности в цитате.
 * @param position Приблизительная позиция цитаты в оригинальном сообщении (в символах).
 * @param isManual True, если цитата была выбрана пользователем вручную.
 *
 * @since 1.0.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record TextQuote(
    @JsonProperty("rich_texts") java.util.List<com.fasterxml.jackson.databind.JsonNode> richTexts,
    @JsonProperty("text") String text,
    @JsonProperty("entities") List<MessageEntity> entities,
    @JsonProperty("position") Integer position,
    @JsonProperty("is_manual") Boolean isManual
) implements BotApiObject {    // BOTAPI104_OLD_RECORD_CONSTRUCTOR
    @Deprecated(since = "1.2.4")
    public TextQuote(String text, List<MessageEntity> entities, Integer position, Boolean isManual) {
        this(null, text, entities, position, isManual);
    }
    /** Typed, lossless representation of Bot API RichText values. */
    @com.fasterxml.jackson.annotation.JsonIgnore
    public java.util.List<pro.kaleert.nyagram.api.objects.richtext.RichTextValue> richTextsDecoded() {
        if (richTexts == null) return java.util.List.of();
        return richTexts.stream().map(pro.kaleert.nyagram.api.objects.richtext.RichTextValue::from).toList();
    }

}