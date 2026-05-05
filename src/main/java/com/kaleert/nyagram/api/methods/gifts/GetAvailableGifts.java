package com.kaleert.nyagram.api.methods.gifts;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.kaleert.nyagram.api.exception.TelegramApiRequestException;
import com.kaleert.nyagram.api.meta.BotApiMethod;
import com.kaleert.nyagram.api.objects.gifts.Gifts;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Возвращает список подарков (Gifts), которые в данный момент можно отправить пользователям.
 * <p>
 * Требует наличия звезд на балансе бота (если подарки платные). Возвращает объект {@link Gifts}.
 * </p>
 *
 * @since 1.1.4
 */
@Data
@Builder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetAvailableGifts extends BotApiMethod<Gifts> {

    public static final String PATH = "getAvailableGifts";

    @Override
    public String getMethod() {
        return PATH;
    }

    @Override
    public Gifts deserializeResponse(String answer) throws TelegramApiRequestException {
        return deserializeResponse(answer, Gifts.class);
    }

    @Override
    public void validate() {
    }
}