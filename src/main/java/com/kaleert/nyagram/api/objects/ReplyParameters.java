package com.kaleert.nyagram.api.objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kaleert.nyagram.api.meta.BotApiObject;
import com.kaleert.nyagram.api.objects.message.MessageEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Описывает параметры для ответа на сообщение.
 * <p>
 * Пришел на замену устаревшему параметру {@code reply_to_message_id}.
 * </p>
 *
 * @since 1.1.5
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReplyParameters implements BotApiObject {
    
    /**
     * ID сообщения, на которое дается ответ.
     */
    @JsonProperty("message_id")
    private Integer messageId;
    
    /**
     * Опционально. Если сообщение, на которое отвечают, находится в другом чате.
     */
    @JsonProperty("chat_id")
    private String chatId;
    
    /**
     * Опционально. Если true, сообщение будет отправлено даже если исходное сообщение не найдено.
     */
    @JsonProperty("allow_sending_without_reply")
    private Boolean allowSendingWithoutReply;
    
    /**
     * Опционально. Текст цитируемой части сообщения.
     */
    @JsonProperty("quote")
    private String quote;
    
    /**
     * Опционально. Режим парсинга цитаты.
     */
    @JsonProperty("quote_parse_mode")
    private String quoteParseMode;
    
    /**
     * Опционально. Сущности в цитируемом тексте.
     */
    @JsonProperty("quote_entities")
    private List<MessageEntity> quoteEntities;
    
    /**
     * Опционально. Позиция цитаты в исходном сообщении (в UTF-16).
     */
    @JsonProperty("quote_position")
    private Integer quotePosition;

    /**
     * Уникальный идентификатор опции опроса, на которую отправляется ответ (API 9.6).
     */
    @JsonProperty("poll_option_id")
    private String pollOptionId;
}