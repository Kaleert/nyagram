package pro.kaleert.nyagram.api.objects.richmessage;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;

/**
 * Содержимое форматированного сообщения Telegram. Используется один из вариантов: blocks, html или markdown.
 * @param blocks Последовательность форматированных блоков.
 * @param html HTML-разметка содержимого.
 * @param markdown Текст с Markdown-разметкой.
 * @param media Вложенные медиаобъекты.
 * @param isRtl Признак направления текста справа налево.
 * @param skipEntityDetection Признак отключения автоматического определения сущностей.
 * @since 1.2.4
 */
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record InputRichMessage(
  @JsonProperty("blocks") List<InputRichBlock> blocks,
  @JsonProperty("html") String html,
  @JsonProperty("markdown") String markdown,
  @JsonProperty("media") List<InputRichMessageMedia> media,
  @JsonProperty("is_rtl") Boolean isRtl,
  @JsonProperty("skip_entity_detection") Boolean skipEntityDetection
) {
    
    /**
     * Создаёт богатое сообщение из HTML-текста.
     *
     * @param html исходное значение html
     * @return объект богатого сообщения с HTML-содержимым
     */
    public static InputRichMessage html(String html) {
        return new InputRichMessage(null,html,null,null,null,null);
    }

    /**
     * Создаёт богатое сообщение из Markdown-текста.
     *
     * @param markdown исходное значение markdown
     * @return объект богатого сообщения с Markdown-содержимым
     */
    public static InputRichMessage markdown(String markdown) {
        return new InputRichMessage(null,null,markdown,null,null,null);
    }
    /**
     * Создаёт богатое сообщение из последовательности структурированных блоков.
     *
     * @param blocks блоки содержимого сообщения
     * @return объект богатого сообщения с указанными блоками
     */
    public static InputRichMessage blocks(List<InputRichBlock> blocks) {
        return new InputRichMessage(blocks,null,null,null,null,null);
    }

    /**
     * Проверяет корректность содержимого богатого сообщения.
     */
    public void validate() {
        int count = (blocks!=null?1:0)+(html!=null?1:0)+(markdown!=null?1:0);
        if(count!=1)throw new IllegalArgumentException("Exactly one of blocks, html, markdown is required");
        if(blocks!=null && blocks.stream().anyMatch(java.util.Objects::isNull))
            throw new IllegalArgumentException("Null blocks not allowed");
        if(media!=null)for(InputRichMessageMedia m:media) m.validate();
    }
}
