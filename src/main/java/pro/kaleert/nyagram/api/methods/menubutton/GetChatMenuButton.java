package pro.kaleert.nyagram.api.methods.menubutton;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import pro.kaleert.nyagram.api.exception.TelegramApiRequestException;
import pro.kaleert.nyagram.api.meta.BotApiMethod;
import pro.kaleert.nyagram.api.objects.menubutton.MenuButton;
import lombok.*;

/**
 * Используйте этот метод для получения текущей настройки кнопки меню в приватном чате.
 * <p>
 * Возвращает объект {@link MenuButton}.
 * </p>
 *
 * @since 1.2.2
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetChatMenuButton extends BotApiMethod<MenuButton> {
    public static final String PATH = "getChatMenuButton";

    @JsonProperty("chat_id")
    private String chatId;

    @Override
    public String getMethod() { return PATH; }

    @Override
    public MenuButton deserializeResponse(String answer) throws TelegramApiRequestException {
        return deserializeResponse(answer, MenuButton.class);
    }

    @Override
    public void validate() {}
}
