package pro.kaleert.nyagram.api.objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import pro.kaleert.nyagram.api.meta.BotApiObject;
import java.util.List;

/**
 * Представляет список фотографий профиля пользователя.
 *
 * @param totalCount Общее количество фотографий профиля у пользователя (может быть больше, чем возвращено в запросе).
 * @param photos Запрошенные фотографии профиля (каждая фото представлена массивом размеров).
 *
 * @see pro.kaleert.nyagram.api.methods.GetUserProfilePhotos
 * @since 1.0.0
 */
public record UserProfilePhotos(
    @JsonProperty("total_count") Integer totalCount,
    @JsonProperty("photos") List<List<PhotoSize>> photos
) implements BotApiObject {}