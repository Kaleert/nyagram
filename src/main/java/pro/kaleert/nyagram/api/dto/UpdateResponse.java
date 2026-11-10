package pro.kaleert.nyagram.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import pro.kaleert.nyagram.api.objects.Update;
import java.util.List;

/**
 * DTO для десериализации ответа метода {@code getUpdates}.
 * <p>
 * Содержит список обновлений {@link Update}.
 * </p>
 *
 * @since 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateResponse {

    /** Признак успешного выполнения запроса Telegram Bot API. */
    private Boolean ok;

    /** Результат успешного запроса; конкретный тип зависит от метода Bot API. */
    private List<Update> result;

    /** Текстовое описание результата или ошибки, возвращённое Telegram. */
    private String description;
    
    /** Числовой код ошибки Telegram Bot API; присутствует при ошибочном ответе. */
    private Integer errorCode;
}