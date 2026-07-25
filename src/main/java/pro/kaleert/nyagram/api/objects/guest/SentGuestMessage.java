package pro.kaleert.nyagram.api.objects.guest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import pro.kaleert.nyagram.api.meta.BotApiObject;

/**
 * Сообщение, успешно отправленное ботом в гостевом режиме.
 *
 * @since 1.2.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record SentGuestMessage(
    @JsonProperty("guest_query_id") String guestQueryId
) implements BotApiObject {}
