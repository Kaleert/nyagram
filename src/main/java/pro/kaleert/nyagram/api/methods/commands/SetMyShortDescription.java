package pro.kaleert.nyagram.api.methods.commands;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import pro.kaleert.nyagram.api.meta.BotApiMethodBoolean;
import lombok.*;

/**
 * Изменяет краткое описание бота (текст, который отображается в профиле бота под именем).
 *
 * @since 1.2.1
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SetMyShortDescription extends BotApiMethodBoolean {
    public static final String PATH = "setMyShortDescription";

    /** Краткое описание (0-120 символов). */
    @JsonProperty("short_description")
    private String shortDescription;

    @JsonProperty("language_code")
    private String languageCode;

    @Override public String getMethod() { return PATH; }
    @Override public void validate() {}
}