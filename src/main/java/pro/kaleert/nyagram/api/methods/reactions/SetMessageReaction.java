package pro.kaleert.nyagram.api.methods.reactions;

import com.fasterxml.jackson.annotation.JsonProperty;
import pro.kaleert.nyagram.api.exception.TelegramApiValidationException;
import pro.kaleert.nyagram.api.meta.BotApiMethodBoolean;
import pro.kaleert.nyagram.api.objects.reactions.ReactionType;
import pro.kaleert.nyagram.api.objects.reactions.ReactionTypeEmoji;
import lombok.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Используйте этот метод для изменения списка реакций на сообщении.
 * <p>
 * Чтобы убрать все реакции, передайте пустой список в поле {@code reaction}.
 * Бот должен быть администратором (если ставит реакции в канале/группе) или быть участником.
 * </p>
 *
 * @since 1.0.0
 */
@EqualsAndHashCode(callSuper = false)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SetMessageReaction extends BotApiMethodBoolean {
    
    /** Имя метода в Telegram Bot API. */
    public static final String PATH = "setMessageReaction";

    /**
     * Уникальный идентификатор чата.
     */
    @JsonProperty("chat_id")
    private String chatId;

    /**
     * Идентификатор сообщения.
     */
    @JsonProperty("message_id")
    private Integer messageId;

    /**
     * Список реакций. Оставьте пустым, чтобы удалить все реакции.
     */
    @JsonProperty("reaction")
    private List<ReactionType> reaction;

    /**
     * Если true, реакция будет отправлена с большой анимацией.
     */
    @JsonProperty("is_big")
    private Boolean isBig;

    @Override
    public String getMethod() {
        return PATH;
    }

    @Override
    public void validate() throws TelegramApiValidationException {
        if (chatId == null || chatId.isEmpty()) {
            throw new TelegramApiValidationException("ChatId обязателен", PATH, "chat_id");
        }
        if (messageId == null) {
            throw new TelegramApiValidationException("MessageId обязателен", PATH, "message_id");
        }
        if (reaction != null) {
            for (ReactionType r : reaction) {
                r.validate();
            }
        }
    }
    
    /**
     * Устанавливает ID чата из Long.
     *
     * @param chatId ID чата.
     */
    public void setChatId(@NonNull Long chatId) {
        this.chatId = chatId.toString();
    }
    
    /**
     * Устанавливает список реакций, заменяя существующие.
     * <p>
     * Удобный метод для передачи списка эмодзи строками.
     * </p>
     *
     * @param emojis Массив строк с эмодзи (например, "👍", "🔥").
     */
    public void setReactions(String... emojis) {
        if (emojis == null || emojis.length == 0) {
            this.reaction = new ArrayList<>();
            return;
        }
        this.reaction = Arrays.stream(emojis)
                .map(e -> ReactionTypeEmoji.builder().emoji(e).build())
                .collect(Collectors.toList());
    }
    
    /**
     * Добавляет одну реакцию (эмодзи) к списку устанавливаемых реакций.
     * <p>
     * Это удобно для добавления реакций по одной, вместо создания полного списка.
     * </p>
     *
     * @param emoji Строка с эмодзи (например, "👍").
     */
    public void addReaction(String emoji) {
        if (this.reaction == null) {
            this.reaction = new ArrayList<>();
        }
        this.reaction.add(ReactionTypeEmoji.builder().emoji(emoji).build());
    }
}