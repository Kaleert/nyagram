package pro.kaleert.nyagram.api.objects.richtext;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;

/**
 * Представление RichText без потери исходного JSON: текст, массив или типизированный объект.
 * @since 1.2.4
 */
public sealed interface RichTextValue permits RichTextValue.Plain, RichTextValue.Sequence,
        RichTextValue.TonAddress, RichTextValue.Tagged {
    
    /** * Возвращает JSON-представление богатого текста.
     *
     * @return JSON-представление значения
     */
    JsonNode json();

    /**
     * Обычный текстовый фрагмент без дополнительного форматирования.
     *
     * @since 1.2.4
     */
    record Plain(String value, JsonNode json) implements RichTextValue {}
    
    /**
     * Последовательность фрагментов форматированного текста, объединённых в одно значение.
     *
     * @since 1.2.4
     */
    record Sequence(List<RichTextValue> elements, JsonNode json) implements RichTextValue {
        public Sequence { elements = List.copyOf(elements); }
    }

    /**
     * Фрагмент форматированного текста, содержащий адрес TON.
     *
     * @since 1.2.4
     */
    record TonAddress(RichTextValue text, String address, JsonNode json) implements RichTextValue {}
    /**
     * Текстовый фрагмент с метаданными форматирования или семантическим тегом.
     *
     * @since 1.2.4
     */
    record Tagged(String type, JsonNode json) implements RichTextValue {
        /** Returns a field for other currently supported RichText variants without losing data. */
        public JsonNode field(String fieldName) { return json.get(fieldName); }
    }

    /**
     * Преобразует JSON-узел в типизированное значение богатого текста.
     *
     * @param node исходное значение node
     * @return типизированное значение богатого текста
     */
    static RichTextValue from(JsonNode node) {
        if (node == null || node.isNull()) throw new IllegalArgumentException("RichText must not be null");
        if (node.isTextual()) return new Plain(node.textValue(), node.deepCopy());
        if (node.isArray()) {
            List<RichTextValue> values = new ArrayList<>();
            for (JsonNode value : node) values.add(from(value));
            return new Sequence(values, node.deepCopy());
        }
        if (!node.isObject()) throw new IllegalArgumentException("RichText must be string, array or object");
        JsonNode typeNode=node.get("type");
        if (typeNode == null || !typeNode.isTextual())
            throw new IllegalArgumentException("RichText object must contain string type");
        String type=typeNode.asText();
        if ("ton_address".equals(type)) {
            JsonNode textNode=node.get("text");
            JsonNode address=node.get("address");
            if (textNode == null || address == null || !address.isTextual())
                throw new IllegalArgumentException("RichTextTonAddress needs text and address");
            return new TonAddress(from(textNode),address.asText(),node.deepCopy());
        }
        return new Tagged(type,node.deepCopy());
    }

    /**
     * Разбирает JSON-представление форматированного текста.
     *
     * @param json исходная JSON-строка
     * @return разобранное значение форматированного текста
     * @throws com.fasterxml.jackson.core.JsonProcessingException если JSON некорректен
     */
    static RichTextValue parse(String json) throws com.fasterxml.jackson.core.JsonProcessingException {
        return from(new ObjectMapper().readTree(json));
    }
}
