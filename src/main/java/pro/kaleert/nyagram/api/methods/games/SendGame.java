package pro.kaleert.nyagram.api.methods.games;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import pro.kaleert.nyagram.api.exception.TelegramApiRequestException;
import pro.kaleert.nyagram.api.exception.TelegramApiValidationException;
import pro.kaleert.nyagram.api.meta.BotApiMethod;
import pro.kaleert.nyagram.api.objects.ReplyParameters;
import pro.kaleert.nyagram.api.objects.message.Message;
import pro.kaleert.nyagram.api.objects.replykeyboard.InlineKeyboardMarkup;
import lombok.*;

/**
 * Используйте этот метод для отправки игры.
 * <p>
 * Игра должна быть предварительно создана через @BotFather.
 * </p>
 *
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SendGame extends BotApiMethod<Message> {

    /** Имя метода в Telegram Bot API. */
    public static final String PATH = "sendGame";

    /**
     * Уникальный идентификатор целевого чата.
     */
    @JsonProperty("chat_id")
    private String chatId;

    /**
     * Уникальный идентификатор топика (для форумов).
     */
    @JsonProperty("message_thread_id")
    private Integer messageThreadId;

    /**
     * Короткое имя игры (short name), заданное при создании в BotFather.
     */
    @JsonProperty("game_short_name")
    private String gameShortName;

    /**
     * Отключить уведомление о сообщении.
     */
    @JsonProperty("disable_notification")
    private Boolean disableNotification;

    /**
     * Защитить контент от пересылки.
     */
    @JsonProperty("protect_content")
    private Boolean protectContent;

    /**
     * Если сообщение является ответом, ID исходного сообщения.
     * @deprecated Используйте {@link #replyParameters} начиная с Nyagram 1.1.5
     */
    @Deprecated(since = "1.1.5")
    @JsonProperty("reply_to_message_id")
    private Integer replyToMessageId;

    /**
     * @deprecated Используйте {@link #replyParameters} начиная с Nyagram 1.1.5
     */
    @Deprecated(since = "1.1.5")
    @JsonProperty("allow_sending_without_reply")
    private Boolean allowSendingWithoutReply;
    
    /**
     * Параметры ответа. Заменяет reply_to_message_id.
     * Необходим для ответов на варианты опросов (API 9.6).
     */
    @JsonProperty("reply_parameters")
    private ReplyParameters replyParameters;

    /**
     * Inline-клавиатура. Если не задана, будет отправлена стандартная кнопка "Play game_title".
     * <p>
     * Если вы отправляете свою клавиатуру, первая кнопка в ней обязательно должна быть типа callbackGame.
     * </p>
     */
    @JsonProperty("reply_markup")
    private InlineKeyboardMarkup replyMarkup;

    @JsonProperty("message_effect_id")
    private String messageEffectId;

    /** Идентификатор бизнес-соединения. */
    @JsonProperty("business_connection_id")
    private String businessConnectionId;

    @Override
    public String getMethod() {
        return PATH;
    }

    @Override
    public Message deserializeResponse(String answer) throws TelegramApiRequestException {
        return deserializeResponse(answer, Message.class);
    }

    @Override
    public void validate() throws TelegramApiValidationException {
        if (chatId == null) throw new TelegramApiValidationException("ChatId обязателен", PATH, "chat_id");
        if (gameShortName == null) throw new TelegramApiValidationException("Short Name игры обязателен", PATH, "game_short_name");
    }
}