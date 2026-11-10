package pro.kaleert.nyagram.api.methods.gifts;
import com.fasterxml.jackson.annotation.*;
import lombok.*;
import pro.kaleert.nyagram.api.exception.*;
import pro.kaleert.nyagram.api.meta.*;

/**
 * Метод удаления подарка, связанного с бизнес-аккаунтом Telegram.
 * @since 1.2.4
 */
@Data @Builder @NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode(callSuper=false)
@JsonIgnoreProperties(ignoreUnknown=true) @JsonInclude(JsonInclude.Include.NON_NULL)
public class DeleteBusinessAccountGift extends BotApiMethodBoolean {
    
    /** Имя метода Telegram Bot API: {@code deleteBusinessAccountGift}. */
    public static final String PATH = "deleteBusinessAccountGift";

    /** Идентификатор подключения бизнес-аккаунта, от имени которого удаляется подарок. */
    @JsonProperty("business_connection_id") private String businessConnectionId;

    /** Идентификатор принадлежащего бизнес-аккаунту подарка. */
    @JsonProperty("owned_gift_id") private String ownedGiftId;

    @Override public String getMethod() { return PATH; }

    @Override public void validate() throws TelegramApiValidationException {
        if (businessConnectionId == null || businessConnectionId.isBlank()) throw new TelegramApiValidationException("businessConnectionId required", PATH);
        if (ownedGiftId == null || ownedGiftId.isBlank()) throw new TelegramApiValidationException("ownedGiftId required", PATH);
    }
}
