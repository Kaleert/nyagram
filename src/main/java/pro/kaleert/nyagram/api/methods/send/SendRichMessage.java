package pro.kaleert.nyagram.api.methods.send;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.*;
import pro.kaleert.nyagram.api.meta.BotApiMethod;
import pro.kaleert.nyagram.api.exception.*;
import pro.kaleert.nyagram.api.objects.*;
import pro.kaleert.nyagram.api.objects.message.*;
import pro.kaleert.nyagram.api.objects.replykeyboard.ReplyKeyboard;

/**
 * Метод sendRichMessage для отправки форматированного сообщения Telegram.
 * @since 1.2.4
 */
@Data @Builder @NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode(callSuper=false)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SendRichMessage extends BotApiMethod<Message> {
    
    /** Имя метода Telegram Bot API: {@code sendRichMessage}. */
    public static final String PATH="sendRichMessage";

    /** Идентификатор бизнес-подключения, от имени которого отправляется сообщение. */
    @JsonProperty("business_connection_id") private String businessConnectionId;

    /** Идентификатор чата-получателя. */
    @JsonProperty("chat_id") private String chatId;

    /** Идентификатор темы форума для сообщения. */
    @JsonProperty("message_thread_id") private Integer messageThreadId;

    /** Идентификатор темы личных сообщений, в которую отправляется сообщение. */
    @JsonProperty("direct_messages_topic_id") private Integer directMessagesTopicId;

    /** Параметры временного сообщения, включая срок его существования. */
    @JsonProperty("ephemeral_message_parameters") private EphemeralMessageParameters ephemeralMessageParameters;

    /** Содержимое богатого сообщения в структурированном формате. */
    @JsonProperty("rich_message") private JsonNode richMessage;

    /** Отправляет сообщение без звукового уведомления. */
    @JsonProperty("disable_notification") private Boolean disableNotification;

    /** Запрещает пересылку и сохранение содержимого сообщения получателем. */
    @JsonProperty("protect_content") private Boolean protectContent;

    /** Разрешает платную рассылку сообщения сверх бесплатных лимитов Telegram. */
    @JsonProperty("allow_paid_broadcast") private Boolean allowPaidBroadcast;

    /** Идентификатор визуального эффекта сообщения. */
    @JsonProperty("message_effect_id") private String messageEffectId;

    /** Параметры предлагаемой публикации для чата, если применимо. */
    @JsonProperty("suggested_post_parameters") private JsonNode suggestedPostParameters;

    /** Параметры ответа на другое сообщение. */
    @JsonProperty("reply_parameters") private ReplyParameters replyParameters;

    /** Клавиатура, прикрепляемая к отправляемому сообщению. */
    @JsonProperty("reply_markup") private ReplyKeyboard replyMarkup;

    @Override public String getMethod(){return PATH;}
    @Override public Message deserializeResponse(String answer) throws TelegramApiRequestException {
        return deserializeResponse(answer,Message.class);
    }
    @Override public void validate() throws TelegramApiValidationException {
        boolean ephemeralCallback=ephemeralMessageParameters!=null
            && ephemeralMessageParameters.callbackQueryId()!=null
            && !ephemeralMessageParameters.callbackQueryId().isBlank();
        if ((chatId==null || chatId.isBlank()) && !ephemeralCallback)
            throw new TelegramApiValidationException("chat_id required unless callback-only ephemeral",PATH);
        if (richMessage==null || !richMessage.isObject())
            throw new TelegramApiValidationException("rich_message must be a JSON object",PATH);
        int supplied=0;
        if (richMessage.hasNonNull("blocks")) {
            if (!richMessage.get("blocks").isArray()) throw new TelegramApiValidationException("blocks must be an array",PATH);
            supplied++;
        }
        if (richMessage.hasNonNull("html")) {
            if (!richMessage.get("html").isTextual()) throw new TelegramApiValidationException("html must be a string",PATH);
            supplied++;
        }
        if (richMessage.hasNonNull("markdown")) {
            if (!richMessage.get("markdown").isTextual()) throw new TelegramApiValidationException("markdown must be a string",PATH);
            supplied++;
        }
        if (supplied!=1) throw new TelegramApiValidationException("rich_message requires exactly one of blocks, html, markdown",PATH);
        if (replyMarkup!=null) replyMarkup.validate();
    }

    /** Type-safe alternative to supplying raw rich_message JSON. The original JsonNode
     * field and Lombok builder are preserved for source compatibility. */
    @com.fasterxml.jackson.annotation.JsonIgnore
    public SendRichMessage withTypedRichMessage(
            pro.kaleert.nyagram.api.objects.richmessage.InputRichMessage value) {
        if(value==null) { this.richMessage=null; return this; }
        value.validate();
        this.richMessage = new com.fasterxml.jackson.databind.ObjectMapper().valueToTree(value);
        return this;
    }

    /** Decode the current raw rich_message to typed blocks, without changing the wire shape. */
    @com.fasterxml.jackson.annotation.JsonIgnore
    public pro.kaleert.nyagram.api.objects.richmessage.InputRichMessage typedRichMessage() {
        if(this.richMessage==null)return null;
        return new com.fasterxml.jackson.databind.ObjectMapper().convertValue(
            this.richMessage,pro.kaleert.nyagram.api.objects.richmessage.InputRichMessage.class);
    }
}
