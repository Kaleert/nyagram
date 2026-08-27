package pro.kaleert.nyagram.api.objects.payments;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import pro.kaleert.nyagram.api.meta.BotApiObject;
import pro.kaleert.nyagram.api.objects.User;
import pro.kaleert.nyagram.api.objects.chat.Chat;

/**
 * Описывает изменение в подписке пользователя на платные услуги бота (Telegram Stars).
 *
 * @since 1.2.2
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record BotSubscriptionUpdated(
    @JsonProperty("user") User user,
    @JsonProperty("chat") Chat chat,
    @JsonProperty("status") String status,
    @JsonProperty("start_date") Integer startDate,
    @JsonProperty("expiration_date") Integer expirationDate,
    @JsonProperty("invoice_payload") String invoicePayload,
    @JsonProperty("subscription_period") Integer subscriptionPeriod,
    @JsonProperty("subscription_price") Integer subscriptionPrice
) implements BotApiObject {}