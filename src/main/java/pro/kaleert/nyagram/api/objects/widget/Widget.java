package pro.kaleert.nyagram.api.objects.widget;

import com.fasterxml.jackson.annotation.*;
import pro.kaleert.nyagram.api.meta.BotApiObject;
import pro.kaleert.nyagram.api.objects.User;
import pro.kaleert.nyagram.api.objects.chat.Chat;

/**
 * Данные виджета Telegram: идентификатор, разметка и связанное состояние.
 * @param id Идентификатор объекта.
 * @param width Ширина.
 * @param height Высота.
 * @param html HTML-разметка содержимого.
 * @param publicData Публичные данные виджета; пустая строка может использоваться для очистки.
 * @param chatData Данные виджета, относящиеся к чату.
 * @since 1.2.4
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record Widget(
    @JsonProperty("id") String id,
    @JsonProperty("width") Integer width,
    @JsonProperty("height") Integer height,
    @JsonProperty("html") String html,
    @JsonProperty("public_data") String publicData,
    @JsonProperty("chat_data") String chatData
) implements BotApiObject {}
