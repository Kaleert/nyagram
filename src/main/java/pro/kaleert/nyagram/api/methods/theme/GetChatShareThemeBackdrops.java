package pro.kaleert.nyagram.api.methods.theme;
import com.fasterxml.jackson.annotation.*;
import lombok.*;
import pro.kaleert.nyagram.api.exception.*;
import pro.kaleert.nyagram.api.meta.*;
import pro.kaleert.nyagram.api.objects.theme.ChatShareThemeBackdrops;

/**
 * Метод получения доступных фонов для тем чатов Telegram.
 * @since 1.2.4
 */
@Data @Builder @NoArgsConstructor @EqualsAndHashCode(callSuper=false)
@JsonIgnoreProperties(ignoreUnknown=true) @JsonInclude(JsonInclude.Include.NON_NULL)
public class GetChatShareThemeBackdrops extends BotApiMethod<ChatShareThemeBackdrops> {
    
    /** Имя метода Telegram Bot API: {@code getChatShareThemeBackdrops}. */
    public static final String PATH = "getChatShareThemeBackdrops";

    @Override public String getMethod() { return PATH; }
    @Override public ChatShareThemeBackdrops deserializeResponse(String answer) throws TelegramApiRequestException { return deserializeResponse(answer, ChatShareThemeBackdrops.class); }
    @Override public void validate() throws TelegramApiValidationException {

    }
}
