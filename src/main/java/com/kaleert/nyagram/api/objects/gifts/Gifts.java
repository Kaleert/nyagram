package com.kaleert.nyagram.api.objects.gifts;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kaleert.nyagram.api.meta.BotApiObject;
import java.util.List;

/**
 * Обертка для списка подарков, доступных для отправки пользователям.
 *
 * @since 1.1.4
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record Gifts(
    @JsonProperty("gifts") List<Gift> gifts
) implements BotApiObject {}