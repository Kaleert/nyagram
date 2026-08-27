package pro.kaleert.nyagram.api.methods.forum;

import com.fasterxml.jackson.annotation.JsonInclude;
import pro.kaleert.nyagram.api.exception.TelegramApiRequestException;
import pro.kaleert.nyagram.api.meta.BotApiMethod;
import pro.kaleert.nyagram.api.objects.stickers.Sticker;
import lombok.*;

import java.util.List;

/**
 * Получает список кастомных эмодзи (стикеров), которые можно использовать как иконки для топиков форума.
 * @since 1.2.2
 */
@Data
@Builder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetForumTopicIconStickers extends BotApiMethod<List<Sticker>> {
    public static final String PATH = "getForumTopicIconStickers";

    @Override
    public String getMethod() { return PATH; }

    @Override
    public List<Sticker> deserializeResponse(String answer) throws TelegramApiRequestException {
        return deserializeResponseArray(answer, Sticker.class);
    }

    @Override public void validate() {}
}
