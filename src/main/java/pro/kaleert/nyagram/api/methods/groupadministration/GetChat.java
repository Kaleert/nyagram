package pro.kaleert.nyagram.api.methods.groupadministration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import pro.kaleert.nyagram.api.exception.TelegramApiRequestException;
import pro.kaleert.nyagram.api.exception.TelegramApiValidationException;
import pro.kaleert.nyagram.api.meta.BotApiMethod;
import pro.kaleert.nyagram.api.objects.chat.ChatFullInfo;
import lombok.*;

/**
 * Используйте этот метод для получения актуальной информации о чате.
 * <p>
 * Возвращает объект {@link ChatFullInfo}, который содержит описание, привязанный канал,
 * локацию, настройки антиспама и доступные реакции.
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
public class GetChat extends BotApiMethod<ChatFullInfo> {
    public static final String PATH = "getChat";

    @JsonProperty("chat_id")
    private String chatId;

    @Override
    public String getMethod() { return PATH; }

    @Override
    public ChatFullInfo deserializeResponse(String answer) throws TelegramApiRequestException {
        return deserializeResponse(answer, ChatFullInfo.class);
    }

    @Override
    public void validate() throws TelegramApiValidationException {
        if (chatId == null || chatId.isEmpty()) throw new TelegramApiValidationException("ChatId is required", PATH);
    }
}
