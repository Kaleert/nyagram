package com.kaleert.nyagram.api.objects.managedbots;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kaleert.nyagram.api.meta.BotApiObject;
import com.kaleert.nyagram.api.objects.User;

/**
 * Апдейт о создании управляемого бота или обновлении его токена.
 @since 1.1.5
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record ManagedBotUpdated(
    @JsonProperty("user") User user,
    @JsonProperty("bot") User bot
) implements BotApiObject {}