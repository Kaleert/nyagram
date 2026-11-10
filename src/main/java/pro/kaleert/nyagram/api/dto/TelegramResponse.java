package pro.kaleert.nyagram.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * DTO для десериализации стандартного ответа Telegram API.
 * <p>
 * Используется для обработки ошибок (когда {@code ok = false}) или простых ответов.
 * </p>
 *
 * @param <T> Тип результата.
 * @since 1.0.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TelegramResponse<T> {

    /** Признак успешного выполнения запроса Telegram Bot API. */
    private Boolean ok;

    /** Результат успешного запроса; конкретный тип зависит от метода Bot API. */
    private T result;

    /** Текстовое описание результата или ошибки, возвращённое Telegram. */
    private String description;
    
    /** Числовой код ошибки Telegram Bot API; присутствует при ошибочном ответе. */
    @JsonProperty("error_code")
    private Integer errorCode;
}