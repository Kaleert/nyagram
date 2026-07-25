package pro.kaleert.nyagram.api.objects.replykeyboard.buttons;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import pro.kaleert.nyagram.api.meta.BotApiObject;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public record KeyboardButtonRequestManagedBot(
    @JsonProperty("request_id") Integer requestId,
    @JsonProperty("suggested_name") String suggestedName,
    @JsonProperty("suggested_username") String suggestedUsername
) implements BotApiObject {}