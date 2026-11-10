package pro.kaleert.nyagram.api.objects.widget;

import com.fasterxml.jackson.annotation.*;
import pro.kaleert.nyagram.api.meta.BotApiObject;
import pro.kaleert.nyagram.api.objects.User;
import pro.kaleert.nyagram.api.objects.chat.Chat;

/**
 * Входящий запрос, инициированный виджетом Telegram.
 * @param id Идентификатор объекта.
 * @param from Поле JSON {@code from} Telegram Bot API.
 * @param chat Поле JSON {@code chat} Telegram Bot API.
 * @param messageId Идентификатор сообщения.
 * @param inlineMessageId Поле JSON {@code inline_message_id} Telegram Bot API.
 * @param chatInstance Поле JSON {@code chat_instance} Telegram Bot API.
 * @param chatType Поле JSON {@code chat_type} Telegram Bot API.
 * @param type Тип объекта Telegram API.
 * @param data Передаваемые данные.
 * @param widgetId Идентификатор виджета.
 * @since 1.2.4
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record WidgetQuery(
    @JsonProperty("id") String id,
    @JsonProperty("from") User from,
    @JsonProperty("chat") Chat chat,
    @JsonProperty("message_id") Integer messageId,
    @JsonProperty("inline_message_id") String inlineMessageId,
    @JsonProperty("chat_instance") String chatInstance,
    @JsonProperty("chat_type") String chatType,
    @JsonProperty("type") WidgetQueryType type,
    @JsonProperty("data") String data,
    @JsonProperty("widget_id") String widgetId
) implements BotApiObject {}
