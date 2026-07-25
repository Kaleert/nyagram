package pro.kaleert.nyagram.api.methods.updates;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import pro.kaleert.nyagram.api.meta.BotApiMethodBoolean;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Используйте этот метод для закрытия инстанса бота перед перемещением его на другой локальный сервер.
 *
 * @since 1.1.5
 */
@Data
@Builder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Close extends BotApiMethodBoolean {
    public static final String PATH = "close";

    @Override
    public String getMethod() {
        return PATH;
    }

    @Override
    public void validate() {}
}