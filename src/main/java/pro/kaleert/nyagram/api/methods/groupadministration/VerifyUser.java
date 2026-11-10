package pro.kaleert.nyagram.api.methods.groupadministration;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import pro.kaleert.nyagram.api.exception.TelegramApiValidationException;
import pro.kaleert.nyagram.api.meta.BotApiMethodBoolean;
import lombok.*;

/**
 * Позволяет боту верифицировать пользователя (выдать синюю галочку) от лица своей организации.
 * <p>
 * <b>Внимание:</b> Этот метод доступен только для ботов, которые привязаны к верифицированным
 * организациям (компаниям, государственным органам и т.д.) через платформу Telegram.
 * </p>
 *
 * @since 1.1.4
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VerifyUser extends BotApiMethodBoolean {

    /** Имя метода Telegram Bot API: {@code verifyUser}. */
    public static final String PATH = "verifyUser";

    /** Уникальный идентификатор пользователя, которого нужно верифицировать. */
    @JsonProperty("user_id")
    private Long userId;

    /** 
     * Кастомное описание для галочки верификации (0-70 символов). 
     * Например: "Официальный сотрудник Nyagram". 
     */
    @JsonProperty("custom_description")
    private String customDescription;

    /** Режим разбора разметки пользовательского описания верификации пользователя. */
    @JsonProperty("custom_description_parse_mode") private String customDescriptionParseMode;
    
    /** Сущности форматирования пользовательского описания верификации пользователя. */
    @JsonProperty("custom_description_entities") private java.util.List<pro.kaleert.nyagram.api.objects.message.MessageEntity> customDescriptionEntities;

    @Override
    public String getMethod() {
        return PATH;
    }

    @Override
    public void validate() throws TelegramApiValidationException {
        if (userId == null) {
            throw new TelegramApiValidationException("UserId is required", PATH);
        }
        if (customDescription != null && customDescription.length() > 70) {
            throw new TelegramApiValidationException("Custom description must be <= 70 chars", PATH);
        }
    }
}