package com.kaleert.nyagram.api.objects.managedbots;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kaleert.nyagram.api.meta.BotApiObject;
import com.kaleert.nyagram.api.objects.User;

/**
 * Служебное сообщение: создан управляемый бот.
 @since 1.1.5
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record ManagedBotCreated(
    @JsonProperty("bot") User bot
) implements BotApiObject {}