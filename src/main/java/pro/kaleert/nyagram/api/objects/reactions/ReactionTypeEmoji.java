package pro.kaleert.nyagram.api.objects.reactions;

import com.fasterxml.jackson.annotation.JsonProperty;
import pro.kaleert.nyagram.api.exception.TelegramApiValidationException;
import lombok.*;

/**
 * Реакция на основе стандартного эмодзи.
 *
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReactionTypeEmoji implements ReactionType {
    private static final String TYPE = "emoji";

    @JsonProperty("type")
    private final String type = TYPE;

    /**
     * Символ эмодзи (например, "👍", "❤").
     */
    @JsonProperty("emoji")
    private String emoji;

    @Override
    public void validate() throws TelegramApiValidationException {
        if (emoji == null || emoji.isEmpty()) {
            throw new TelegramApiValidationException("Emoji cannot be empty", "ReactionTypeEmoji", "emoji");
        }
    }
}