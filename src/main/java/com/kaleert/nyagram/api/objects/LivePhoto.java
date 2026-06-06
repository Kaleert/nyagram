package com.kaleert.nyagram.api.objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kaleert.nyagram.api.meta.BotApiObject;

/**
 * Представляет "живое фото" (фотографию с коротким видео).
 *
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
