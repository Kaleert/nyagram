package com.kaleert.nyagram.api.objects.media;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kaleert.nyagram.api.objects.PhotoSize;
import com.kaleert.nyagram.api.objects.Video;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
record PaidMediaPreview(
    @JsonProperty("width") Integer width,
    @JsonProperty("height") Integer height,
    @JsonProperty("duration") Integer duration
) implements PaidMedia {
    @Override public String getType() { return "preview"; }
}

@JsonIgnoreProperties(ignoreUnknown = true)
record PaidMediaPhoto(
    @JsonProperty("photo") List<PhotoSize> photo
) implements PaidMedia {
    @Override public String getType() { return "photo"; }
}

@JsonIgnoreProperties(ignoreUnknown = true)
record PaidMediaVideo(
    @JsonProperty("video") Video video
) implements PaidMedia {
    @Override public String getType() { return "video"; }
}