package pro.kaleert.nyagram.api.objects.replykeyboard.buttons;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import pro.kaleert.nyagram.api.meta.BotApiObject;

/**
 * Настройки кнопки клавиатуры для запроса создания или выбора управляемого бота. Telegram возвращает результат с соответствующим идентификатором запроса.
 *
 * @since 1.2.4
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public record KeyboardButtonRequestManagedBot(
    @JsonProperty("request_id") Integer requestId,
    @JsonProperty("suggested_name") String suggestedName,
    @JsonProperty("suggested_username") String suggestedUsername
) implements BotApiObject {}