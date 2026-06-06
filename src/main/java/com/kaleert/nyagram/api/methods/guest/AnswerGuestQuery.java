package com.kaleert.nyagram.api.methods.guest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kaleert.nyagram.api.exception.TelegramApiRequestException;
import com.kaleert.nyagram.api.exception.TelegramApiValidationException;
import com.kaleert.nyagram.api.meta.BotApiMethod;
import com.kaleert.nyagram.api.objects.guest.SentGuestMessage;
import lombok.*;

/**
 * Ответ на запросы в гостевом режиме (Guest Mode).
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
public class AnswerGuestQuery extends BotApiMethod<SentGuestMessage> {

    public static final String PATH = "answerGuestQuery";

    @JsonProperty("guest_query_id")
    private String guestQueryId;

    @JsonProperty("text")
    private String text;

    @Override
    public String getMethod() { return PATH; }

    @Override
    public SentGuestMessage deserializeResponse(String answer) throws TelegramApiRequestException {
        return deserializeResponse(answer, SentGuestMessage.class);
    }

    @Override
    public void validate() throws TelegramApiValidationException {
        if (guestQueryId == null || guestQueryId.isEmpty()) {
            throw new TelegramApiValidationException("GuestQueryId is required", PATH);
        }
    }
}
