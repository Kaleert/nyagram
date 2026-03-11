package com.kaleert.nyagram.api.methods.groupadministration;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kaleert.nyagram.api.exception.TelegramApiValidationException;
import com.kaleert.nyagram.api.meta.BotApiMethodBoolean;
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