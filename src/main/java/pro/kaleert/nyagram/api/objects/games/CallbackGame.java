package pro.kaleert.nyagram.api.objects.games;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import pro.kaleert.nyagram.api.meta.BotApiObject;

/**
 * Заглушка (placeholder), используемая в объекте {@link pro.kaleert.nyagram.api.objects.replykeyboard.buttons.InlineKeyboardButton}.
 * <p>
 * Указывает, что кнопка должна запустить игру.
 * </p>
 *
 * @since 1.0.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record CallbackGame() implements BotApiObject {}