package pro.kaleert.nyagram.api.objects.message;
import com.fasterxml.jackson.annotation.*;
import pro.kaleert.nyagram.api.meta.BotApiObject;
import java.util.List;

/**
 * Сведения о сообщениях, для которых отменена плата в Telegram Stars.
 *
 * @since 1.2.4
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record PaidMessagesWaived(
) implements BotApiObject {}
