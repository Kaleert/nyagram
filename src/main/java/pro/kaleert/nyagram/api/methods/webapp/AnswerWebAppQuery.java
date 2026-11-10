package pro.kaleert.nyagram.api.methods.webapp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import pro.kaleert.nyagram.api.exception.TelegramApiRequestException;
import pro.kaleert.nyagram.api.exception.TelegramApiValidationException;
import pro.kaleert.nyagram.api.meta.BotApiMethod;
import pro.kaleert.nyagram.api.meta.BotApiObject;
import pro.kaleert.nyagram.api.objects.inlinequery.result.InlineQueryResult;
import lombok.*;

/**
 * Задает результат взаимодействия пользователя с Web App (Mini App) и отправляет соответствующее
 * inline-сообщение от лица пользователя в чат.
 *
 * @since 1.2.2
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AnswerWebAppQuery extends BotApiMethod<AnswerWebAppQuery.SentWebAppMessage> {
    
    /** Имя метода Telegram Bot API: {@code answerWebAppQuery}. */
    public static final String PATH = "answerWebAppQuery";

    /** Идентификатор Web App-запроса, на который отвечает бот. */
    @JsonProperty("web_app_query_id")
    private String webAppQueryId;

    /** Инлайн-результат, отправляемый в ответ на Web App-запрос. */
    @JsonProperty("result")
    private InlineQueryResult result;

    @Override
    public String getMethod() { return PATH; }

    @Override
    public SentWebAppMessage deserializeResponse(String answer) throws TelegramApiRequestException {
        return deserializeResponse(answer, SentWebAppMessage.class);
    }

    @Override
    public void validate() throws TelegramApiValidationException {
        if (webAppQueryId == null || webAppQueryId.isEmpty()) throw new TelegramApiValidationException("WebAppQueryId required", PATH);
        if (result == null) throw new TelegramApiValidationException("Result is required", PATH);
    }

    /**
     * Результат отправки сообщения от имени пользователя после ответа на запрос Web App.
     * @param inlineMessageId Идентификатор отправленного инлайн-сообщения; может отсутствовать.
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record SentWebAppMessage(
        @JsonProperty("inline_message_id") String inlineMessageId
    ) implements BotApiObject {}
}
