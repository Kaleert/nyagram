package pro.kaleert.nyagram.api.objects.system;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import pro.kaleert.nyagram.api.meta.BotApiObject;

/**
 * Содержит информацию о чате, которым пользователь поделился с ботом.
 * <p>
 * Приходит в ответ на нажатие кнопки {@link pro.kaleert.nyagram.api.objects.replykeyboard.buttons.KeyboardButtonRequestChat}.
 * </p>
 *
 * @param requestId Идентификатор запроса (из кнопки).
 * @param chatId Идентификатор чата, которым поделились.
 *
 * @since 1.0.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record ChatShared(
    @JsonProperty("request_id") Integer requestId,
    @JsonProperty("chat_id") Long chatId
) implements BotApiObject {}