package pro.kaleert.nyagram.api.objects.media;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import pro.kaleert.nyagram.api.objects.PhotoSize;
import pro.kaleert.nyagram.api.objects.Video;
import java.util.List;

/**
 * Предпросмотр платного медиафайла до его покупки пользователем.
 * @param width Ширина миниатюры в пикселях, если доступна.
 * @param height Высота миниатюры в пикселях, если доступна.
 * @param duration Длительность видео в секундах, если доступна.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
record PaidMediaPreview(
    @JsonProperty("width") Integer width,
    @JsonProperty("height") Integer height,
    @JsonProperty("duration") Integer duration
) implements PaidMedia {
    @Override public String getType() { return "preview"; }
}

/**
 * Фотография, доступная после покупки платного медиасообщения.
 * @param photo Доступные размеры фотографии.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
record PaidMediaPhoto(
    @JsonProperty("photo") List<PhotoSize> photo
) implements PaidMedia {
    @Override public String getType() { return "photo"; }
}

/**
 * Видео, доступное после покупки платного медиасообщения.
 * @param video Сведения о видеофайле.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
record PaidMediaVideo(
    @JsonProperty("video") Video video
) implements PaidMedia {
    @Override public String getType() { return "video"; }
}