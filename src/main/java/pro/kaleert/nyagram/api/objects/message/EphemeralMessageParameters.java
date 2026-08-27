package pro.kaleert.nyagram.api.objects.message;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import pro.kaleert.nyagram.api.meta.BotApiObject;
import lombok.Builder;

/**
 * Описывает параметры для эфемерных (исчезающих) сообщений.
 * 
 * @since 1.2.2
 */
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public record EphemeralMessageParameters(
    @JsonProperty("replace_callback_query_message") Boolean replaceCallbackQueryMessage
) implements BotApiObject {}