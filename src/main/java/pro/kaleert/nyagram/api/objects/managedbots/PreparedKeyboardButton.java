package pro.kaleert.nyagram.api.objects.managedbots;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import pro.kaleert.nyagram.api.meta.BotApiObject;

/**
 * Подготовленная кнопка для использования внутри Mini Apps.
 @since 1.1.5
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record PreparedKeyboardButton(
    @JsonProperty("id") String id
) implements BotApiObject {}