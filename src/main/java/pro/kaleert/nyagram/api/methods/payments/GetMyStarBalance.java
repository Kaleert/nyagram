package pro.kaleert.nyagram.api.methods.payments;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import pro.kaleert.nyagram.api.exception.TelegramApiRequestException;
import pro.kaleert.nyagram.api.meta.BotApiMethod;
import pro.kaleert.nyagram.api.objects.payments.StarBalance;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Используйте этот метод для получения текущего баланса Telegram Stars вашего бота.
 * <p>
 * Возвращает объект {@link StarBalance}.
 * </p>
 *
 * @since 1.2.1
 */
@Data
@Builder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetMyStarBalance extends BotApiMethod<StarBalance> {

    public static final String PATH = "getMyStarBalance";

    @Override
    public String getMethod() {
        return PATH;
    }

    @Override
    public StarBalance deserializeResponse(String answer) throws TelegramApiRequestException {
        return deserializeResponse(answer, StarBalance.class);
    }

    @Override
    public void validate() {}
}