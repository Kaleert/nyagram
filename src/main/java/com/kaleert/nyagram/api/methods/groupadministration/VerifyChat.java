package com.kaleert.nyagram.api.methods.groupadministration;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kaleert.nyagram.api.exception.TelegramApiValidationException;
import com.kaleert.nyagram.api.meta.BotApiMethodBoolean;
import lombok.*;

/**
 * Позволяет боту верифицировать чат, супергруппу или канал (выдать синюю галочку) от лица своей организации.
 * <p>
 * <b>Внимание:</b> Этот метод доступен только для ботов, которые привязаны к верифицированным
 * организациям через платформу Telegram.
 * </p>
 *
 * @since 1.1.4
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VerifyChat extends BotApiMethodBoolean {

    public static final String PATH = "verifyChat";

    /** Уникальный идентификатор целевого чата или username канала (в формате @channelusername). */
    @JsonProperty("chat_id")
    private String chatId;

    /** 
     * Кастомное описание для галочки верификации (0-70 символов). 
     * Например: "Официальный канал техподдержки". 
     */
    @JsonProperty("custom_description")
    private String customDescription;

    @Override
    public String getMethod() {
        return PATH;
    }

    @Override
    public void validate() throws TelegramApiValidationException {
        if (chatId == null || chatId.isEmpty()) {
            throw new TelegramApiValidationException("ChatId is required", PATH);
        }
        if (customDescription != null && customDescription.length() > 70) {
            throw new TelegramApiValidationException("Custom description must be <= 70 chars", PATH);
        }
    }
    
    /**
     * Создает базовый запрос на верификацию чата.
     *
     * @param chatId ID чата (Long).
     * @return готовый объект запроса.
     */
    public static VerifyChat of(Long chatId) {
        return VerifyChat.builder().chatId(chatId.toString()).build();
    }
}