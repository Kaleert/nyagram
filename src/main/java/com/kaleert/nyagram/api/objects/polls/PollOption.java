package com.kaleert.nyagram.api.objects.polls;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kaleert.nyagram.api.meta.BotApiObject;
import com.kaleert.nyagram.api.objects.User;
import com.kaleert.nyagram.api.objects.chat.Chat;
import com.kaleert.nyagram.api.objects.message.MessageEntity;

import java.util.List;

/**
 * Вариант ответа в существующем опросе.
 *
 * @param text Текст ответа.
 * @param textEntities Сущности в тексте ответа.
 * @param voterCount Количество проголосовавших за этот вариант.
 * @param persistentId Уникальный персистентный идентификатор опции.
 * @param addedByUser Пользователь, который добавил эту опцию.
 * @param addedByChat Чат, который добавил эту опцию.
 * @param additionDate Unix-время, когда опция была добавлена.
 *
 * @since 1.0.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record PollOption(
    @JsonProperty("text") String text,
    @JsonProperty("text_entities") List<MessageEntity> textEntities,
    @JsonProperty("voter_count") Integer voterCount,
    @JsonProperty("persistent_id") String persistentId,
    @JsonProperty("added_by_user") User addedByUser,
    @JsonProperty("added_by_chat") Chat addedByChat,
    @JsonProperty("addition_date") Integer additionDate
) implements BotApiObject {}