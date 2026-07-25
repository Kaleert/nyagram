package pro.kaleert.nyagram.api.objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import pro.kaleert.nyagram.api.meta.BotApiObject;

/**
 * Простой объект, содержащий уникальный идентификатор сообщения.
 * Используется как возвращаемое значение в методах {@code copyMessage}.
 *
 * @param messageId Уникальный идентификатор сообщения.
 *
 * @since 1.0.0
 */
public record MessageId(
    @JsonProperty("message_id") Integer messageId
) implements BotApiObject {}