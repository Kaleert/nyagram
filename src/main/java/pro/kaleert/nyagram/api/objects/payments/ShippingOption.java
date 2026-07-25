package pro.kaleert.nyagram.api.objects.payments;

import com.fasterxml.jackson.annotation.JsonProperty;
import pro.kaleert.nyagram.api.meta.BotApiObject;
import java.util.List;

/**
 * Представляет один из вариантов доставки.
 *
 * @param id Уникальный идентификатор опции.
 * @param title Название опции (например, "Express Delivery").
 * @param prices Список компонентов цены доставки.
 *
 * @see pro.kaleert.nyagram.api.methods.invoices.AnswerShippingQuery
 * @since 1.0.0
 */
public record ShippingOption(
    @JsonProperty("id") String id,
    @JsonProperty("title") String title,
    @JsonProperty("prices") List<LabeledPrice> prices
) implements BotApiObject {}