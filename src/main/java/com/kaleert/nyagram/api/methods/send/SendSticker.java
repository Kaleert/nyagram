package com.kaleert.nyagram.api.methods.send;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kaleert.nyagram.api.exception.TelegramApiRequestException;
import com.kaleert.nyagram.api.exception.TelegramApiValidationException;
import com.kaleert.nyagram.api.meta.BotApiMethod;
import com.kaleert.nyagram.api.meta.MultipartRequest;
import com.kaleert.nyagram.api.objects.InputFile;
import com.kaleert.nyagram.api.objects.ReplyParameters;
import com.kaleert.nyagram.api.objects.message.Message;
import com.kaleert.nyagram.api.objects.replykeyboard.ReplyKeyboard;
import lombok.*;

import java.io.File;
import java.util.Map;

/**
 * Используйте этот метод для отправки стикеров.
 * <p>
 * Поддерживаются статические (.webp), анимированные (.tgs) и видео (.webm) стикеры.
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
public class SendSticker extends BotApiMethod<Message> implements MultipartRequest {
    
    /** Имя метода в Telegram Bot API. */
    public static final String PATH = "sendSticker";

    /**
     * Уникальный идентификатор чата.
     */
    @JsonProperty("chat_id")
    private String chatId;

    /**
     * Идентификатор топика.
     */
    @JsonProperty("message_thread_id")
    private Integer messageThreadId;

    /**
     * Стикер для отправки (file_id, url или файл).
     */
    @JsonProperty("sticker")
    private InputFile sticker;

    /**
     * Эмодзи, связанный со стикером.
     */
    @JsonProperty("emoji")
    private String emoji;

    /**
     * Отключить уведомление.
     */
    @JsonProperty("disable_notification")
    private Boolean disableNotification;

    /**
     * Защитить контент.
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
     * Клавиатура.
     */
    @JsonProperty("reply_markup")
    private ReplyKeyboard replyMarkup;

    /** Идентификатор эффекта сообщения (например, ID анимации огня или сердечек). */
    @JsonProperty("message_effect_id")
    private String messageEffectId;

    /** Идентификатор бизнес-соединения. */
    @JsonProperty("business_connection_id")
    private String businessConnectionId;
    
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
        if (chatId == null || chatId.isEmpty()) {
            throw new TelegramApiValidationException("ChatId обязателен", PATH, "chat_id");
        }
        if (sticker == null) {
            throw new TelegramApiValidationException("Sticker обязателен", PATH, "sticker");
        }
        sticker.validate();
    }
    
    /**
     * Устанавливает уникальный идентификатор чата.
     *
     * @param chatId ID чата (Long).
     */
    public void setChatId(Long chatId) {
        this.chatId = chatId.toString();
    }
    
    /**
     * Устанавливает эмодзи, соответствующий стикеру.
     * <p>
     * Помогает Telegram предлагать этот стикер при вводе эмодзи.
     * </p>
     *
     * @param emoji Строка с эмодзи (например, "👍").
     * @return текущий билдер.
     */
    public SendSticker emoji(String emoji) {
        this.emoji = emoji;
        return this;
    }
    
    /**
     * Создает запрос на отправку стикера по его идентификатору (file_id).
     *
     * @param chatId ID чата.
     * @param fileId Идентификатор файла на серверах Telegram.
     * @return готовый объект запроса.
     */
    public static SendSticker withFileId(Long chatId, String fileId) {
        return SendSticker.builder()
                .chatId(chatId.toString())
                .sticker(new InputFile(fileId))
                .build();
    }
    
    /**
     * Создает запрос на отправку стикера с диска.
     * <p>
     * Поддерживаются форматы .webp (статичные), .tgs (анимированные) и .webm (видео).
     * </p>
     *
     * @param chatId ID чата.
     * @param file Локальный файл.
     * @return готовый объект запроса.
     */
    public static SendSticker withFile(Long chatId, File file) {
        return SendSticker.builder()
                .chatId(chatId.toString())
                .sticker(new InputFile(file))
                .build();
    }
    
    @Override
    public Map<String, InputFile> getFiles() {
        return sticker != null ? Map.of("sticker", sticker) : Map.of();
    }
    
    /**
     * Разрешить платную рассылку этого сообщения (0.1 Star за сообщение).
     * @return текущий билдер.
     * @since 1.2.0
     */
    public SendSticker paidBroadcast() {
        this.allowPaidBroadcast = true;
        return this;
    }
}