package pro.kaleert.nyagram.api.methods.gifts;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.*;
import pro.kaleert.nyagram.api.exception.*;
import pro.kaleert.nyagram.api.meta.BotApiMethod;
/**
 * Метод getUniqueGift для получения уникального подарка Telegram.
 * @since 1.2.4
 */
@Data @Builder @NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true) @JsonInclude(JsonInclude.Include.NON_NULL)
public class GetUniqueGift extends BotApiMethod<pro.kaleert.nyagram.api.objects.gifts.UniqueGift> {
 /** Имя метода Telegram Bot API: {@code getUniqueGift}. */
 public static final String PATH = "getUniqueGift";

 /** Уникальное имя подарка, сведения о котором запрашиваются. */
 @JsonProperty("name") private String name;
 @Override public String getMethod(){return PATH;}
 @Override public pro.kaleert.nyagram.api.objects.gifts.UniqueGift deserializeResponse(String answer) throws TelegramApiRequestException {return deserializeResponse(answer,pro.kaleert.nyagram.api.objects.gifts.UniqueGift.class);}
 @Override public void validate() throws TelegramApiValidationException {if(name==null||name.isBlank()) throw new TelegramApiValidationException("name required",PATH);}
}
