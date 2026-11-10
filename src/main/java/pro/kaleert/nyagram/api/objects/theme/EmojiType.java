package pro.kaleert.nyagram.api.objects.theme;
import com.fasterxml.jackson.annotation.*;
import pro.kaleert.nyagram.api.meta.BotApiObject;
/**
 * Объект Telegram Bot API 10.4: emoji type.
 * @since 1.2.4
 */
@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include=JsonTypeInfo.As.PROPERTY, property="type", visible=true)
@JsonSubTypes({
    @JsonSubTypes.Type(value=EmojiTypeEmoji.class, name="emoji"),
    @JsonSubTypes.Type(value=EmojiTypeCustomEmoji.class, name="custom_emoji")
})
public sealed interface EmojiType extends BotApiObject permits EmojiTypeEmoji, EmojiTypeCustomEmoji {
    
    /** * Возвращает тип эмодзи в теме оформления.
     *
     * @return идентификатор типа эмодзи
     */
    String type();
}
