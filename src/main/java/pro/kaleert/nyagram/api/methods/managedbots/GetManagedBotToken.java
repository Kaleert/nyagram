package pro.kaleert.nyagram.api.methods.managedbots;

import com.fasterxml.jackson.annotation.JsonProperty;
import pro.kaleert.nyagram.api.exception.TelegramApiRequestException;
import pro.kaleert.nyagram.api.exception.TelegramApiValidationException;
import pro.kaleert.nyagram.api.meta.BotApiMethod;
import lombok.*;

/**
 * Получить токен управляемого бота.
   @since 1.1.5
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class GetManagedBotToken extends BotApiMethod<String> {
    public static final String PATH = "getManagedBotToken";

    @JsonProperty("bot_id")
    private Long botId;

    @Override public String getMethod() { return PATH; }
    
    @Override public String deserializeResponse(String answer) throws TelegramApiRequestException {
        return deserializeResponse(answer, String.class);
    }

    @Override public void validate() throws TelegramApiValidationException {
        if (botId == null) throw new TelegramApiValidationException("BotId is required", PATH);
    }
}