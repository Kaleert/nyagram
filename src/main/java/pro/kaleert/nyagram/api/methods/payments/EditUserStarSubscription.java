package pro.kaleert.nyagram.api.methods.payments;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import pro.kaleert.nyagram.api.exception.TelegramApiValidationException;
import pro.kaleert.nyagram.api.meta.BotApiMethodBoolean;
import lombok.*;

/**
 * Позволяет боту отменить или возобновить подписку пользователя, оплаченную в Telegram Stars.
 *
 * @since 1.2.2
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EditUserStarSubscription extends BotApiMethodBoolean {
    public static final String PATH = "editUserStarSubscription";

    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("telegram_payment_charge_id")
    private String telegramPaymentChargeId;

    /** Если True, подписка будет отменена. Если False, отмена будет отозвана (возобновление). */
    @JsonProperty("is_canceled")
    private Boolean isCanceled;

    @Override
    public String getMethod() { return PATH; }

    @Override
    public void validate() throws TelegramApiValidationException {
        if (userId == null) throw new TelegramApiValidationException("UserId is required", PATH);
        if (telegramPaymentChargeId == null || telegramPaymentChargeId.isEmpty()) {
            throw new TelegramApiValidationException("Charge ID is required", PATH);
        }
        if (isCanceled == null) throw new TelegramApiValidationException("isCanceled is required", PATH);
    }
}
