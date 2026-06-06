package com.kaleert.nyagram.api.methods.reactions;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kaleert.nyagram.api.exception.TelegramApiValidationException;
import com.kaleert.nyagram.api.meta.BotApiMethodBoolean;
import lombok.*;

/**
 * Удаление всех реакций с сообщения.
 *
 * @since 1.2.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeleteAllMessageReactions extends BotApiMethodBoolean {

    public static final String PATH = "deleteAllMessageReactions";

    @JsonProperty("chat_id")
    private String chatId;

    @JsonProperty("message_id")
    private Integer messageId;

    @Override
    public String getMethod() { return PATH; }

    @Override
    public void validate() throws TelegramApiValidationException {
        if (chatId == null || chatId.isEmpty()) throw new TelegramApiValidationException("ChatId required", PATH);
        if (messageId == null) throw new TelegramApiValidationException("MessageId required", PATH);
    }
}
