package pro.kaleert.nyagram.api.objects.message;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import pro.kaleert.nyagram.api.meta.BotApiObject;
import lombok.Builder;

/**
 * Параметры эфемерного ответа на обновление или callback-запрос.
 * @param receiverUserId Идентификатор пользователя-получателя.
 * @param callbackQueryId Идентификатор callback-запроса.
 * @param replaceCallbackQueryMessage Поле JSON {@code replace_callback_query_message} Telegram Bot API.
 * @since 1.2.4
 */
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public record EphemeralMessageParameters(
    @JsonProperty("receiver_user_id") Long receiverUserId,
    @JsonProperty("callback_query_id") String callbackQueryId,
    @JsonProperty("replace_callback_query_message") Boolean replaceCallbackQueryMessage
) implements BotApiObject {}