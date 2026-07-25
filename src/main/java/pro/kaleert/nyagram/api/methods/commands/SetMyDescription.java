package pro.kaleert.nyagram.api.methods.commands;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import pro.kaleert.nyagram.api.meta.BotApiMethodBoolean;
import lombok.*;

/**
 * Изменяет описание бота (текст, который видят пользователи на пустом экране перед нажатием "Start").
 *
 * @since 1.2.1
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SetMyDescription extends BotApiMethodBoolean {
    public static final String PATH = "setMyDescription";

    /** Описание (0-512 символов). */
    @JsonProperty("description")
    private String description;

    @JsonProperty("language_code")
    private String languageCode;

    @Override public String getMethod() { return PATH; }
    @Override public void validate() {}
}