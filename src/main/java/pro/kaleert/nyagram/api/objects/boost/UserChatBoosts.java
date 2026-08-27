package pro.kaleert.nyagram.api.objects.boost;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import pro.kaleert.nyagram.api.meta.BotApiObject;
import java.util.List;

/**
 * Обертка для списка бустов пользователя в чате.
 * @since 1.2.2
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record UserChatBoosts(
    @JsonProperty("boosts") List<ChatBoost> boosts
) implements BotApiObject {}
