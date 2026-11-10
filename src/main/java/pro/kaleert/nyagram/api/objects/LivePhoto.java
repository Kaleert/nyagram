package pro.kaleert.nyagram.api.objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import pro.kaleert.nyagram.api.meta.BotApiObject;

/**
 * Представляет "живое фото" (фотографию с коротким видео).
 *
 *
 * @param fileId Идентификатор файла для повторного использования через Telegram Bot API.
 * @param fileUniqueId Уникальный идентификатор файла, не предназначенный для скачивания.
 * @param width Ширина изображения в пикселях.
 * @param height Высота изображения в пикселях.
 * @param thumbnail Миниатюра живой фотографии.
 * @param fileSize Размер файла в байтах, если известен.
 * @since 1.2.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record LivePhoto(
    @JsonProperty("file_id") String fileId,
    @JsonProperty("file_unique_id") String fileUniqueId,
    @JsonProperty("width") Integer width,
    @JsonProperty("height") Integer height,
    @JsonProperty("thumbnail") PhotoSize thumbnail,
    @JsonProperty("file_size") Long fileSize
) implements BotApiObject {}
