package pro.kaleert.nyagram.api.methods.business;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import pro.kaleert.nyagram.api.exception.TelegramApiRequestException;
import pro.kaleert.nyagram.api.exception.TelegramApiValidationException;
import pro.kaleert.nyagram.api.meta.BotApiMethod;
import pro.kaleert.nyagram.api.objects.Update.BusinessConnection;
import lombok.*;

/**
 * Используйте этот метод для получения информации о подключении бота к бизнес-аккаунту пользователя.
 * <p>
 * Возвращает объект {@link BusinessConnection} при успехе.
 * </p>
 *
 * @since 1.2.2
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetBusinessConnection extends BotApiMethod<BusinessConnection> {
    public static final String PATH = "getBusinessConnection";

    /** Уникальный идентификатор бизнес-соединения. */
    @JsonProperty("business_connection_id")
    private String businessConnectionId;

    @Override
    public String getMethod() { return PATH; }

    @Override
    public BusinessConnection deserializeResponse(String answer) throws TelegramApiRequestException {
        return deserializeResponse(answer, BusinessConnection.class);
    }

    @Override
    public void validate() throws TelegramApiValidationException {
        if (businessConnectionId == null || businessConnectionId.isEmpty()) {
            throw new TelegramApiValidationException("BusinessConnectionId is required", PATH);
        }
    }
}
