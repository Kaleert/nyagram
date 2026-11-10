package pro.kaleert.nyagram.api.objects.media;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import pro.kaleert.nyagram.api.meta.BotApiObject;
import pro.kaleert.nyagram.api.meta.Validable;
import pro.kaleert.nyagram.api.objects.InputFile;

/**
 * Базовый интерфейс для платных медиафайлов (Paid Media).
 * <p>
 * Используется в методе {@code sendPaidMedia}.
 * </p>
 *
 * @since 1.0.0
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = InputPaidMediaPhoto.class, name = "photo"),
    @JsonSubTypes.Type(value = InputPaidMediaVideo.class, name = "video")
})
@JsonInclude(JsonInclude.Include.NON_NULL)
public sealed interface InputPaidMedia extends BotApiObject, Validable 
    permits InputPaidMediaPhoto, InputPaidMediaVideo {
    
    /**
     * Возвращает тип платного медиа для сериализации в Telegram Bot API.
     *
     * @return тип платного медиа (photo или video)
     */
    String getType();

    /**
     * Возвращает строковый источник платного медиафайла.
     *
     * @return file_id, URL или attach-идентификатор медиа
     */
    String getMedia();

    /**
     * Возвращает локальный файл для отправки платного медиа через multipart.
     *
     * @return локальный файл или null, если он не используется
     */
    InputFile getMediaFile();
    
    /**
     * Создаёт или обновляет платное медиа с указанным строковым источником.
     *
     * @param media новый file_id, URL или attach-идентификатор медиа
     * @return объект платного медиа с заданным источником
     */
    InputPaidMedia withMedia(String media);
}