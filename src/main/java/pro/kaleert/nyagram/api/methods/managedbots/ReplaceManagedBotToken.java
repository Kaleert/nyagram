/* telegram/nyagram-1.1.5-(indev)/src/main/java/com/kaleert/nyagram/api/methods/managedbots/ReplaceManagedBotToken.java */
package pro.kaleert.nyagram.api.methods.managedbots;

import com.fasterxml.jackson.annotation.JsonProperty;
import pro.kaleert.nyagram.api.exception.TelegramApiRequestException;
import pro.kaleert.nyagram.api.exception.TelegramApiValidationException;
import pro.kaleert.nyagram.api.meta.BotApiMethod;
import lombok.*;

/**
 * Сгенерировать новый токен управляемому боту (инвалидируя старый) (API 9.6).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ReplaceManagedBotToken extends BotApiMethod<String> {
    public static final String PATH = "replaceManagedBotToken";

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