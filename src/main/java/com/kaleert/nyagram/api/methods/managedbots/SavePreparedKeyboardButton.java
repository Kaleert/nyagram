package com.kaleert.nyagram.api.methods.managedbots;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kaleert.nyagram.api.exception.TelegramApiRequestException;
import com.kaleert.nyagram.api.exception.TelegramApiValidationException;
import com.kaleert.nyagram.api.meta.BotApiMethod;
import com.kaleert.nyagram.api.objects.managedbots.PreparedKeyboardButton;
import com.kaleert.nyagram.api.objects.replykeyboard.buttons.KeyboardButton;
import lombok.*;

/**
 * Сохранить кнопку, чтобы запросить юзеров/чаты/ботов из Mini Apps.
 @since 1.1.5
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class SavePreparedKeyboardButton extends BotApiMethod<PreparedKeyboardButton> {
    public static final String PATH = "savePreparedKeyboardButton";

    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("button")
    private KeyboardButton button;

    @Override public String getMethod() { return PATH; }
    
    @Override public PreparedKeyboardButton deserializeResponse(String answer) throws TelegramApiRequestException {
        return deserializeResponse(answer, PreparedKeyboardButton.class);
    }

    @Override public void validate() throws TelegramApiValidationException {
        if (userId == null) throw new TelegramApiValidationException("UserId is required", PATH);
        if (button == null) throw new TelegramApiValidationException("Button is required", PATH);
    }
}