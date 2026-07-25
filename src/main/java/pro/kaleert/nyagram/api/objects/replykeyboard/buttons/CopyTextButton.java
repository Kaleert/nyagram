package pro.kaleert.nyagram.api.objects.replykeyboard.buttons;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import pro.kaleert.nyagram.api.meta.BotApiObject;
import pro.kaleert.nyagram.api.meta.Validable;
import pro.kaleert.nyagram.api.exception.TelegramApiValidationException;

/**
 * Описывает inline-кнопку, которая копирует заданный текст в буфер обмена пользователя.
 * <p>
 * Введено в Telegram Bot API 9.5. Отлично подходит для копирования промокодов,
 * кошельков, ID транзакций или длинных команд.
 * </p>
 *
 * @param text Текст, который будет скопирован в буфер обмена (1-256 символов).
 *
 * @since 1.1.4
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record CopyTextButton(
    @JsonProperty("text") String text
) implements BotApiObject, Validable {

    @Override
    public void validate() throws TelegramApiValidationException {
        if (text == null || text.isEmpty()) {
            throw new TelegramApiValidationException("Copy text cannot be empty", "CopyTextButton");
        }
        if (text.length() > 256) {
            throw new TelegramApiValidationException("Copy text must be <= 256 characters", "CopyTextButton");
        }
    }
}