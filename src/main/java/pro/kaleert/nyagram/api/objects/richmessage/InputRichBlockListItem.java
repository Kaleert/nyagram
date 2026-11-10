package pro.kaleert.nyagram.api.objects.richmessage;
import com.fasterxml.jackson.annotation.*;
import java.util.List;
/**
 * Элемент списка форматированного сообщения, включая вложенные блоки и состояние флажка.
 * @param blocks Последовательность форматированных блоков.
 * @param hasCheckbox Признак наличия флажка у элемента списка.
 * @param isChecked Признак выбранного флажка.
 * @param value Поле JSON {@code value} Telegram Bot API.
 * @param type Тип объекта Telegram API.
 * @since 1.2.4
 */
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record InputRichBlockListItem(
  @JsonProperty("blocks") List<InputRichBlock> blocks,
  @JsonProperty("has_checkbox") Boolean hasCheckbox,
  @JsonProperty("is_checked") Boolean isChecked,
  @JsonProperty("value") Integer value,
  @JsonProperty("type") String type
) {}
