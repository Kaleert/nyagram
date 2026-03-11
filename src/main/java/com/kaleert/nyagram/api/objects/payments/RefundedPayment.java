package com.kaleert.nyagram.api.objects.payments;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kaleert.nyagram.api.meta.BotApiObject;

/**
 * Информация о возвращенном платеже.
 *
 * @since 1.1.4
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record RefundedPayment(
    @JsonProperty("currency") String currency,
    @JsonProperty("total_amount") Integer totalAmount,
    @JsonProperty("invoice_payload") String invoicePayload,
    @JsonProperty("telegram_payment_charge_id") String telegramPaymentChargeId,
    @JsonProperty("provider_payment_charge_id") String providerPaymentChargeId
) implements BotApiObject {}