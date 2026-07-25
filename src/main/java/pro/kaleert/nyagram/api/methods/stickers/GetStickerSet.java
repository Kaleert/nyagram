package pro.kaleert.nyagram.api.methods.stickers;

import com.fasterxml.jackson.annotation.JsonProperty;
import pro.kaleert.nyagram.api.exception.TelegramApiRequestException;
import pro.kaleert.nyagram.api.exception.TelegramApiValidationException;
import pro.kaleert.nyagram.api.meta.BotApiMethod;
import pro.kaleert.nyagram.api.objects.stickers.StickerSet;
import lombok.*;

/**
 * Используйте этот метод для получения набора стикеров.
 * <p>
 * Возвращает объект {@link StickerSet}.
 * </p>
 *
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class GetStickerSet extends BotApiMethod<StickerSet> {
        
    /** Имя метода в Telegram Bot API. */
    public static final String PATH = "getStickerSet";

    /**
     * Название набора стикеров (short name).
     */
    @JsonProperty("name") 
    private String name;

    @Override public String getMethod() { return PATH; }

    @Override
    public StickerSet deserializeResponse(String answer) throws TelegramApiRequestException {
        return deserializeResponse(answer, StickerSet.class);
    }

    @Override
    public void validate() throws TelegramApiValidationException {
        if (name == null || name.isEmpty()) throw new TelegramApiValidationException("Name is required", PATH);
    }
}