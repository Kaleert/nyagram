package pro.kaleert.nyagram.api.objects.media;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import pro.kaleert.nyagram.api.meta.BotApiObject;

/**
 * Базовый интерфейс для платных медиафайлов, купленных за Telegram Stars.
 *
 * @since 1.1.4
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = PaidMediaPreview.class, name = "preview"),
    @JsonSubTypes.Type(value = PaidMediaPhoto.class, name = "photo"),
    @JsonSubTypes.Type(value = PaidMediaVideo.class, name = "video")
})
@JsonInclude(JsonInclude.Include.NON_NULL)
public sealed interface PaidMedia extends BotApiObject permits PaidMediaPreview, PaidMediaPhoto, PaidMediaVideo {
    String getType();
}