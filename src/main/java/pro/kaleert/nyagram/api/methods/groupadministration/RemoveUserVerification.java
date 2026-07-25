package pro.kaleert.nyagram.api.methods.groupadministration;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import pro.kaleert.nyagram.api.exception.TelegramApiValidationException;
import pro.kaleert.nyagram.api.meta.BotApiMethodBoolean;
import lombok.*;

/**
 * Отзывает верификацию (синюю галочку) у пользователя, ранее выданную ботом от лица организации.
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
public class RemoveUserVerification extends BotApiMethodBoolean {

    public static final String PATH = "removeUserVerification";

    /** Уникальный идентификатор пользователя, у которого нужно забрать верификацию. */
    @JsonProperty("user_id")
    private Long userId;

    @Override
    public String getMethod() {
        return PATH;
    }

    @Override
    public void validate() throws TelegramApiValidationException {
        if (userId == null) {
            throw new TelegramApiValidationException("UserId is required", PATH);
        }
    }
    
    /**
     * Создает запрос на снятие верификации с пользователя.
     *
     * @param userId ID пользователя.
     * @return готовый объект запроса.
     */
    public static RemoveUserVerification of(Long userId) {
        return RemoveUserVerification.builder().userId(userId).build();
    }
}