package pro.kaleert.nyagram.api.objects.media;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import pro.kaleert.nyagram.api.meta.BotApiObject;
import pro.kaleert.nyagram.api.meta.Validable;
import pro.kaleert.nyagram.api.objects.InputFile;
import pro.kaleert.nyagram.api.objects.message.MessageEntity;

import java.util.List;

/**
 * Базовый интерфейс, представляющий содержимое медиа-сообщения для отправки в альбоме (Media Group).
 *
 * @see pro.kaleert.nyagram.api.methods.send.SendMediaGroup
 * @since 1.0.0
 */
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = InputMediaPhoto.class, name = "photo"),
    @JsonSubTypes.Type(value = InputMediaVideo.class, name = "video"),
    @JsonSubTypes.Type(value = InputMediaAudio.class, name = "audio"),
    @JsonSubTypes.Type(value = InputMediaDocument.class, name = "document")
})
@JsonInclude(JsonInclude.Include.NON_NULL)
public sealed interface InputMedia extends BotApiObject, Validable 
    permits InputMediaPhoto, InputMediaVideo, InputMediaAudio, InputMediaDocument {

    /**
     * Возвращает тип медиафайла, используемый при сериализации в Telegram Bot API.
     *
     * @return тип медиафайла (например, photo или video)
     */
    String getType();

    /**
     * Возвращает ссылку, file_id или attach-идентификатор отправляемого медиафайла.
     *
     * @return строковое представление источника медиа
     */
    String getMedia();

    /**
     * Возвращает подпись, связанную с медиафайлом.
     *
     * @return текст подписи или null, если подпись не задана
     */
    String getCaption();

    /**
     * Возвращает режим разбора форматирования подписи.
     *
     * @return режим форматирования или null, если он не задан
     */
    String getParseMode();

    /**
     * Возвращает явно заданные сущности форматирования подписи.
     *
     * @return список сущностей или null, если они не заданы
     */
    List<MessageEntity> getCaptionEntities();

    /**
     * Возвращает локальный файл, используемый для multipart-загрузки медиа.
     *
     * @return локальный файл или null, если медиа задано строкой
     */
    @JsonIgnore
    InputFile getMediaFile();
    
    /**
     * Возвращает локальный файл миниатюры для multipart-загрузки, если он поддерживается данным типом медиа.
     *
     * @return файл миниатюры или null при его отсутствии
     */
    @JsonIgnore
    InputFile getThumbnail();

    /**
     * Создаёт или обновляет описание медиа с указанным строковым источником.
     * Конкретный тип сохраняет собственную реализацию этого метода.
     *
     * @param newMediaString новый file_id, URL или attach-идентификатор медиа
     * @return объект медиа с заданным источником
     */
    InputMedia withMedia(String newMediaString);
}