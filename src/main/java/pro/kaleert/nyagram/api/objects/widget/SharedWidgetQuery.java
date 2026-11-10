package pro.kaleert.nyagram.api.objects.widget;

import com.fasterxml.jackson.annotation.*;
import pro.kaleert.nyagram.api.meta.BotApiObject;
import pro.kaleert.nyagram.api.objects.User;
import pro.kaleert.nyagram.api.objects.chat.Chat;

/**
 * Входящий запрос к общему виджету Telegram.
 * @param id Идентификатор объекта.
 * @param from Поле JSON {@code from} Telegram Bot API.
 * @param type Тип объекта Telegram API.
 * @param data Передаваемые данные.
 * @param widgetId Идентификатор виджета.
 * @since 1.2.4
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record SharedWidgetQuery(
    @JsonProperty("id") String id,
    @JsonProperty("from") User from,
    @JsonProperty("type") WidgetQueryType type,
    @JsonProperty("data") String data,
    @JsonProperty("widget_id") String widgetId
) implements BotApiObject {}
