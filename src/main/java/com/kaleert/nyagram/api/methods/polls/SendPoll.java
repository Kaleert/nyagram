package com.kaleert.nyagram.api.methods.polls;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kaleert.nyagram.api.exception.TelegramApiRequestException;
import com.kaleert.nyagram.api.exception.TelegramApiValidationException;
import com.kaleert.nyagram.api.meta.BotApiMethod;
import com.kaleert.nyagram.api.objects.ReplyParameters;
import com.kaleert.nyagram.api.objects.message.Message;
import com.kaleert.nyagram.api.objects.message.MessageEntity;
import com.kaleert.nyagram.api.objects.polls.InputPollOption;
import com.kaleert.nyagram.api.objects.replykeyboard.ReplyKeyboard;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Используйте этот метод для отправки нативных опросов (Polls).
 * <p>
 * Опросы могут быть двух типов: "regular" (обычный) и "quiz" (викторина с правильным ответом).
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
public class SendPoll extends BotApiMethod<Message> {
    
    /** Имя метода в Telegram Bot API. */
    public static final String PATH = "sendPoll";

    /**
     * Уникальный идентификатор чата.
     */
    @JsonProperty("chat_id")
    private String chatId;

    /**
     * Вопрос опроса (1-300 символов).
     */
    @JsonProperty("question")
    private String question;

    /**
     * Список вариантов ответов (2-10 строк).
     */
    @JsonProperty("options")
    private List<InputPollOption> options;

    /**
     * Если true, опрос будет анонимным.
     */
    @JsonProperty("is_anonymous")
    private Boolean isAnonymous;

    /**
     * Тип опроса: "regular" или "quiz". По умолчанию "regular".
     */
    @JsonProperty("type")
    private String type;

    /**
     * Если true, можно выбрать несколько вариантов ответа. (Только для regular).
     */
    @JsonProperty("allows_multiple_answers")
    private Boolean allowsMultipleAnswers;

    /**
     * Индекс правильного ответа (от 0). Обязательно для quiz.
     */
    @JsonProperty("correct_option_id")
    private Integer correctOptionId;

    /**
     * Объяснение, которое увидит пользователь, выбравший неправильный ответ (для quiz).
     */
    @JsonProperty("explanation")
    private String explanation;

    /**
     * Время жизни опроса в секундах (5-600). Нельзя использовать вместе с close_date.
     */
    @JsonProperty("open_period")
    private Integer openPeriod;

    /**
     * Точная дата закрытия опроса (Unix timestamp).
     */
    @JsonProperty("close_date")
    private Integer closeDate;

    /**
     * Если true, опрос будет отправлен сразу закрытым.
     */
    @JsonProperty("is_closed")
    private Boolean isClosed;

    /**
     * Отключить уведомление.
     */
    @JsonProperty("disable_notification")
    private Boolean disableNotification;

    /**
     * Клавиатура.
     */
    @JsonProperty("reply_markup")
    private ReplyKeyboard replyMarkup;
    
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
    
    /** Идентификатор эффекта сообщения (например, ID анимации огня или сердечек). */
    @JsonProperty("message_effect_id")
    private String messageEffectId;

    /** Идентификатор бизнес-соединения. */
    @JsonProperty("business_connection_id")
    private String businessConnectionId;
    
    @JsonProperty("allows_revoting")
    private Boolean allowsRevoting;

    @JsonProperty("shuffle_options")
    private Boolean shuffleOptions;

    @JsonProperty("allow_adding_options")
    private Boolean allowAddingOptions;

    @JsonProperty("hide_results_until_closes")
    private Boolean hideResultsUntilCloses;

    @JsonProperty("description")
    private String description;

    @JsonProperty("description_parse_mode")
    private String descriptionParseMode;

    @JsonProperty("description_entities")
    private List<MessageEntity> descriptionEntities;
    
    /**
    * @since 1.2.0
    */
    @JsonProperty("media")
    private Object media;
        
    /**
    * @since 1.2.0
    */
    @JsonProperty("explanation_media")
    private Object explanationMedia;
    
    /**
    * @since 1.2.0
    */
    @JsonProperty("members_only")
    private Boolean membersOnly;
    
    /**
    * @since 1.2.0
    */
    @JsonProperty("country_codes")
    private List<String> countryCodes;
    
    /**
     * Позволяет отправить сообщение даже тем пользователям, которые заблокировали бота, 
     * за счет списания Telegram Stars с баланса бота.
     * @since 1.2.0
     */
     @JsonProperty("allow_paid_broadcast")
    private Boolean allowPaidBroadcast;
    
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
        if (question == null || question.isEmpty()) throw new TelegramApiValidationException("Вопрос обязателен", PATH, "question");
        if (options == null || options.size() < 2 || options.size() > 10) {
            throw new TelegramApiValidationException("Опрос должен иметь от 2 до 10 вариантов", PATH, "options");
        }
        if ("quiz".equals(type) && correctOptionId == null) {
            throw new TelegramApiValidationException("Для викторины нужен правильный ответ (correct_option_id)", PATH, "correct_option_id");
        }
    }
    
    /**
     * Добавляет один вариант ответа в список опций.
     *
     * @param text Текст варианта ответа.
     * @return текущий билдер.
     */
    public SendPoll addOption(String text) {
        if (this.options == null) this.options = new ArrayList<>();
        this.options.add(InputPollOption.of(text));
        return this;
    }
    
    /**
     * Настраивает опрос как викторину (quiz).
     *
     * @param correctOptionId Индекс правильного ответа (начиная с 0), соответствующий списку опций.
     * @return текущий билдер.
     */
    public SendPoll asQuiz(int correctOptionId) {
        this.type = "quiz";
        this.correctOptionId = correctOptionId;
        return this;
    }
    
    /**
     * Настраивает опрос как "регулярный" (regular) с возможностью выбора нескольких вариантов ответа.
     *
     * @return текущий билдер.
     */
    public SendPoll allowMultiple() {
        this.type = "regular";
        this.allowsMultipleAnswers = true;
        return this;
    }
    
    /**
     * Создает базовый запрос на отправку опроса (по умолчанию тип "regular").
     *
     * @param chatId ID чата (Long).
     * @param question Вопрос.
     * @return готовый объект запроса.
     */
    public static SendPoll of(Long chatId, String question) {
        return SendPoll.builder()
                .chatId(chatId.toString())
                .question(question)
                .type("regular")
                .build();
    }
    
    /**
     * Разрешить платную рассылку этого сообщения (0.1 Star за сообщение).
     * @return текущий билдер.
     * @since 1.2.0
     */
    public SendPoll paidBroadcast() {
        this.allowPaidBroadcast = true;
        return this;
    }
}