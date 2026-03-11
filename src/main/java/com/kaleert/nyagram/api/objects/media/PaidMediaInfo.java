package com.kaleert.nyagram.api.objects.media;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kaleert.nyagram.api.meta.BotApiObject;
import java.util.List;

/**
 * Содержит информацию о платных медиа в сообщении.
 *
 * @since 1.1.4
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record PaidMediaInfo(
    @JsonProperty("star_count") Integer starCount,
    @JsonProperty("paid_media") List<PaidMedia> paidMedia
) implements BotApiObject {}
