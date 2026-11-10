package pro.kaleert.nyagram.api.objects.theme;
import com.fasterxml.jackson.annotation.*;
import pro.kaleert.nyagram.api.meta.BotApiObject;
/**
 * Тема оформления, используемая при публикации в чат Telegram.
 * @since 1.2.4
 */
@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include=JsonTypeInfo.As.PROPERTY, property="type", visible=true)
@JsonSubTypes({
    @JsonSubTypes.Type(value=ChatShareThemeEmoji.class, name="emoji"),
    @JsonSubTypes.Type(value=ChatShareThemeUniqueGift.class, name="unique_gift")
})
public sealed interface ChatShareTheme extends BotApiObject permits ChatShareThemeEmoji, ChatShareThemeUniqueGift {
    
    /** * Возвращает тип темы оформления общего чата.
     *
     * @return идентификатор типа темы
     */
    String type();
}
