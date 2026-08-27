package pro.kaleert.nyagram.api.methods.commands;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import pro.kaleert.nyagram.api.exception.TelegramApiRequestException;
import pro.kaleert.nyagram.api.meta.BotApiMethod;
import pro.kaleert.nyagram.api.objects.ChatAdministratorRights;
import lombok.*;

/**
 * Получает права администратора по умолчанию, которые бот запрашивает при добавлении в чат.
 * @since 1.2.2
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetMyDefaultAdministratorRights extends BotApiMethod<ChatAdministratorRights> {
    public static final String PATH = "getMyDefaultAdministratorRights";

    @JsonProperty("for_channels")
    private Boolean forChannels;

    @Override
    public String getMethod() { return PATH; }

    @Override
    public ChatAdministratorRights deserializeResponse(String answer) throws TelegramApiRequestException {
        return deserializeResponse(answer, ChatAdministratorRights.class);
    }

    @Override public void validate() {}
}
