package com.kaleert.nyagram.api.methods.updates;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kaleert.nyagram.api.meta.BotApiMethodBoolean;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Используйте этот метод для выхода из облачного сервера Telegram Bot API перед запуском локального.
 *
 * @since 1.1.5
 */
@Data
@Builder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class LogOut extends BotApiMethodBoolean {
    public static final String PATH = "logOut";

    @Override
    public String getMethod() {
        return PATH;
    }

    @Override
    public void validate() {}
}