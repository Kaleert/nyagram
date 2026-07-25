package pro.kaleert.nyagram.api.objects.reactions;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import pro.kaleert.nyagram.api.meta.BotApiObject;
import pro.kaleert.nyagram.api.meta.Validable;

/**
 * Базовый интерфейс, описывающий тип реакции (эмодзи).
 *
 * @since 1.0.0
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type",
        defaultImpl = Void.class
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ReactionTypeEmoji.class, name = "emoji"),
        @JsonSubTypes.Type(value = ReactionTypeCustomEmoji.class, name = "custom_emoji"),
        @JsonSubTypes.Type(value = ReactionTypePaid.class, name = "paid")
})
public interface ReactionType extends BotApiObject, Validable {
    String getType();
}