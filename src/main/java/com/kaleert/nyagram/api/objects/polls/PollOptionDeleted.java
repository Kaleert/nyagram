package com.kaleert.nyagram.api.objects.polls;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kaleert.nyagram.api.meta.BotApiObject;
import com.kaleert.nyagram.api.objects.message.MessageEntity;
import com.kaleert.nyagram.api.objects.message.MaybeInaccessibleMessage;

import java.util.List;

/**
 * Этот объект представляет удаление опции из опроса.
 *
 * @since 1.1.5
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record PollOptionDeleted(
    @JsonProperty("option_persistent_id") String optionPersistentId,
    @JsonProperty("option_text") String optionText,
    @JsonProperty("option_text_entities") List<MessageEntity> optionTextEntities,
    @JsonProperty("poll_message") MaybeInaccessibleMessage pollMessage
) implements BotApiObject {}