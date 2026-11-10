package pro.kaleert.nyagram.api.methods.commands;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import pro.kaleert.nyagram.api.meta.BotApiMethodBoolean;
import pro.kaleert.nyagram.api.objects.ChatAdministratorRights;
import lombok.*;

/**
 * Изменяет права администратора по умолчанию, которые запрашиваются ботом при его добавлении
 * в группу или канал.
 *
 * @since 1.2.2
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SetMyDefaultAdministratorRights extends BotApiMethodBoolean {
    
    /** Имя метода Telegram Bot API: {@code setMyDefaultAdministratorRights}. */
    public static final String PATH = "setMyDefaultAdministratorRights";

    /** Права администратора, которые бот будет запрашивать по умолчанию. */
    @JsonProperty("rights")
    private ChatAdministratorRights rights;

    /** Если true, устанавливает права по умолчанию для каналов; иначе — для групп. */
    @JsonProperty("for_channels")
    private Boolean forChannels;

    @Override
    public String getMethod() { return PATH; }

    @Override
    public void validate() {}
}
