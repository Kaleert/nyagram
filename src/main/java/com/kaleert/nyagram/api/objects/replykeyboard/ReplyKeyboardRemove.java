package com.kaleert.nyagram.api.objects.replykeyboard;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

/**
 * При получении сообщения с этим объектом, клиенты Telegram удалят текущую кастомную клавиатуру
 * и покажут стандартную буквенно-цифровую клавиатуру.
 * <p>
 * Это эквивалентно тому, как если бы пользователь нажал кнопку "Закрыть" на клавиатуре.
 * </p>
 *
 * @param removeKeyboard Обязательно True. Запрашивает удаление клавиатуры.
 * @param selective      Опционально. Если True, клавиатура будет удалена только у конкретных пользователей.
 *                       (например, у тех, кого упомянули в тексте, или кому ответили).
 *
 * @since 1.1.2
 */
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public record ReplyKeyboardRemove(
    @JsonProperty("remove_keyboard") Boolean removeKeyboard,
    @JsonProperty("selective") Boolean selective
) implements ReplyKeyboard {

    /**
     * Создает объект для удаления клавиатуры.
     *
     * @param selective Если true, удаляет клавиатуру выборочно.
     * @return готовый объект.
     */
    public static ReplyKeyboardRemove of(boolean selective) {
        return new ReplyKeyboardRemove(true, selective);
    }

    /**
     * Создает объект для удаления клавиатуры у всех.
     *
     * @return готовый объект.
     */
    public static ReplyKeyboardRemove all() {
        return new ReplyKeyboardRemove(true, false);
    }

    @Override
    public void validate() {
    }
}
