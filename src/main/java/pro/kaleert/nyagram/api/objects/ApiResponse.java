package pro.kaleert.nyagram.api.objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import pro.kaleert.nyagram.api.meta.BotApiObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Представляет ответ от Telegram Bot API.
 *
 * @param <T> Тип результата.
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> implements BotApiObject {

    /** Имя JSON-поля признака успешного запроса: {@code ok}. */
    private static final String OK_FIELD = "ok";

    /** Имя JSON-поля результата запроса: {@code result}. */
    private static final String RESULT_FIELD = "result";

    /** Имя JSON-поля кода ошибки: {@code error_code}. */
    private static final String ERROR_CODE_FIELD = "error_code";

    /** Имя JSON-поля описания ошибки: {@code description}. */
    private static final String DESCRIPTION_FIELD = "description";

    /** Имя JSON-поля дополнительных параметров ошибки: {@code parameters}. */
    private static final String PARAMETERS_FIELD = "parameters";

    /**
     * True, если запрос был успешным.
     */
    @JsonProperty(OK_FIELD)
    private Boolean ok;

    /**
     * Результат запроса (если ok=True).
     */
    @JsonProperty(RESULT_FIELD)
    private T result;

    /**
     * Код ошибки (если ok=False).
     */
    @JsonProperty(ERROR_CODE_FIELD)
    private Integer errorCode;

    /**
     * Описание ошибки в человекочитаемом формате.
     */
    @JsonProperty(DESCRIPTION_FIELD)
    private String description;
    
    /**
     * Дополнительные параметры ошибки (например, retry_after при лимитах).
     */
    @JsonProperty(PARAMETERS_FIELD)
    private ResponseParameters parameters;
}