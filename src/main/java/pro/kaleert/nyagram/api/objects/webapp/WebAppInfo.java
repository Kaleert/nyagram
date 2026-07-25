package pro.kaleert.nyagram.api.objects.webapp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import pro.kaleert.nyagram.api.meta.BotApiObject;
import pro.kaleert.nyagram.api.meta.Validable;
import pro.kaleert.nyagram.api.exception.TelegramApiValidationException;

/**
 * Описывает Web App.
 * <p>
 * Используется в кнопках клавиатуры для открытия мини-приложений.
 * </p>
 *
 * @param url HTTPS URL, который будет открыт в Web App.
 *
 * @since 1.0.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record WebAppInfo(
    @JsonProperty("url") String url
) implements BotApiObject, Validable {

    @Override
    public void validate() throws TelegramApiValidationException {
        if (url == null || url.isBlank()) {
            throw new TelegramApiValidationException("WebApp URL cannot be empty", "WebAppInfo");
        }
    }
}