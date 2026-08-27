package pro.kaleert.nyagram.api.objects.inlinequery;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import pro.kaleert.nyagram.api.meta.BotApiObject;

/**
 * Подготовленное инлайн-сообщение для отправки пользователем из Mini App.
 * @since 1.2.2
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record PreparedInlineMessage(
    @JsonProperty("id") String id,
    @JsonProperty("expiration_date") Integer expirationDate
) implements BotApiObject {}