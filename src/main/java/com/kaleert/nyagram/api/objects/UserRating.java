package com.kaleert.nyagram.api.objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kaleert.nyagram.api.meta.BotApiObject;

/**
 * Описывает рейтинг пользователя на основе его трат Telegram Stars.
 *
 * @since 1.1.4
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record UserRating(
    @JsonProperty("level") Integer level,
    @JsonProperty("rating") Integer rating,
    @JsonProperty("current_level_rating") Integer currentLevelRating,
    @JsonProperty("next_level_rating") Integer nextLevelRating
) implements BotApiObject {}