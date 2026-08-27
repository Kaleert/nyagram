package pro.kaleert.nyagram.api.methods.send;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import pro.kaleert.nyagram.api.exception.TelegramApiValidationException;
import pro.kaleert.nyagram.api.meta.BotApiMethodBoolean;
import pro.kaleert.nyagram.api.objects.LinkPreviewOptions;
import pro.kaleert.nyagram.api.objects.ReplyParameters;
import pro.kaleert.nyagram.api.objects.message.MessageEntity;
import lombok.*;

import java.util.List;

/**
 * Устанавливает черновик (Draft) сообщения в поле ввода пользователя в чате.
 *
 * @since 1.2.2
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SendMessageDraft extends BotApiMethodBoolean {
    public static final String PATH = "sendMessageDraft";

    @JsonProperty("chat_id") private String chatId;
    @JsonProperty("message_thread_id") private Integer messageThreadId;
    
    /** В API 10.3 текст может быть пустым для очистки черновика. */
    @JsonProperty("text") private String text;
    @JsonProperty("parse_mode") private String parseMode;
    @JsonProperty("entities") private List<MessageEntity> entities;
    @JsonProperty("link_preview_options") private LinkPreviewOptions linkPreviewOptions;
    @JsonProperty("reply_parameters") private ReplyParameters replyParameters;
    
    /** Остановить генерацию черновика (например, если бот "печатает"). */
    @JsonProperty("can_stop") private Boolean canStop;
    /** Сохранять черновик, если генерация остановлена. */
    @JsonProperty("keep_on_stop") private Boolean keepOnStop;

    @Override public String getMethod() { return PATH; }

    @Override public void validate() throws TelegramApiValidationException {
        if (chatId == null || chatId.isEmpty()) throw new TelegramApiValidationException("ChatId is required", PATH);
    }
}