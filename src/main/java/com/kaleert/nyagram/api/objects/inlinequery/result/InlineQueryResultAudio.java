package com.kaleert.nyagram.api.objects.inlinequery.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kaleert.nyagram.api.objects.inlinequery.inputmessagecontent.InputMessageContent;
import com.kaleert.nyagram.api.objects.replykeyboard.InlineKeyboardMarkup;
import lombok.Builder;

/**
 * Представляет ссылку на MP3-файл.
 * <p>
 * По умолчанию этот результат будет отправлен пользователем как аудиофайл.
 * </p>
 *
 * @param id Уникальный идентификатор результата.
 * @param audioUrl Действительный URL аудиофайла.
 * @param title Заголовок трека.
 * @param performer Исполнитель.
 * @param audioDuration Длительность аудио в секундах.
 * @param caption Подпись к аудио (0-1024 символов).
 * @param parseMode Режим парсинга подписи.
 * @param replyMarkup Inline-клавиатура.
 * @param inputMessageContent Содержимое сообщения, которое будет отправлено вместо аудио (опционально).
 * @param thumbnailUrl Обложка трека
 *
 * @since 1.1.2
 */
@Builder
public record InlineQueryResultAudio(
    @JsonProperty("id") String id,
    @JsonProperty("audio_url") String audioUrl,
    @JsonProperty("title") String title,
    @JsonProperty("performer") String performer,
    @JsonProperty("audio_duration") Integer audioDuration,
    @JsonProperty("caption") String caption,
    @JsonProperty("parse_mode") String parseMode,
    @JsonProperty("reply_markup") InlineKeyboardMarkup replyMarkup,
    @JsonProperty("input_message_content") InputMessageContent inputMessageContent,
    @JsonProperty("thumbnail_url") String thumbnailUrl
) implements InlineQueryResult {

    @Override
    public String getType() {
        return "audio";
    }

    @Override public String getId() { return id; }
    @Override public InlineKeyboardMarkup getReplyMarkup() { return replyMarkup; }
}
