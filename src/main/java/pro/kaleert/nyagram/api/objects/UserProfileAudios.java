package pro.kaleert.nyagram.api.objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import pro.kaleert.nyagram.api.meta.BotApiObject;
import java.util.List;

/**
 * Представляет список аудиозаписей, добавленных в профиль пользователя.
 *
 * @since 1.1.4
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record UserProfileAudios(
    @JsonProperty("total_count") Integer totalCount,
    @JsonProperty("audios") List<Audio> audios
) implements BotApiObject {}