package pro.kaleert.nyagram.api.objects.payments;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import pro.kaleert.nyagram.api.meta.BotApiObject;

/**
 * Описывает текущий баланс Telegram Stars (Звезд) бота.
 *
 * @since 1.2.1
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record StarBalance(
    @JsonProperty("balance") Long balance
) implements BotApiObject {}