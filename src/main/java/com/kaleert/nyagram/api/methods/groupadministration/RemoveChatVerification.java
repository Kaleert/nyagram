package com.kaleert.nyagram.api.methods.groupadministration;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kaleert.nyagram.api.exception.TelegramApiValidationException;
import com.kaleert.nyagram.api.meta.BotApiMethodBoolean;
import lombok.*;

/**
 * Отзывает верификацию (синюю галочку) у чата или канала, ранее выданную ботом от лица организации.
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
public class RemoveChatVerification extends BotApiMethodBoolean {

    public static final String PATH = "removeChatVerification";

    /** Уникальный идентификатор целевого чата или username канала. */
    @JsonProperty("chat_id")
    private String chatId;

    @Override
    public String getMethod() {
        return PATH;
    }

    @Override
    public void validate() throws TelegramApiValidationException {
        if (chatId == null || chatId.isEmpty()) {
            throw new TelegramApiValidationException("ChatId is required", PATH);
        }
    }
    
    /**
     * Создает запрос на снятие верификации с чата.
     *
     * @param chatId ID чата (Long).
     * @return готовый объект запроса.
     */
    public static RemoveChatVerification of(Long chatId) {
        return RemoveChatVerification.builder().chatId(chatId.toString()).build();
    }
}