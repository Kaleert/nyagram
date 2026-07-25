package pro.kaleert.nyagram.api.methods.commands;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import pro.kaleert.nyagram.api.meta.BotApiMethodBoolean;
import lombok.*;

/**
 * Изменяет имя бота (отображается в списке контактов и сверху в чате).
 *
 * @since 1.2.1
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SetMyName extends BotApiMethodBoolean {
    public static final String PATH = "setMyName";

    /** Новое имя бота (0-64 символа). Передайте пустое, чтобы удалить. */
    @JsonProperty("name")
    private String name;

    /** Двухбуквенный код языка (например, "ru"). */
    @JsonProperty("language_code")
    private String languageCode;

    @Override public String getMethod() { return PATH; }
    @Override public void validate() {}
}