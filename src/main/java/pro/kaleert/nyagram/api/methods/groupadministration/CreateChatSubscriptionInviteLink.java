package pro.kaleert.nyagram.api.methods.groupadministration;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import pro.kaleert.nyagram.api.exception.TelegramApiRequestException;
import pro.kaleert.nyagram.api.exception.TelegramApiValidationException;
import pro.kaleert.nyagram.api.meta.BotApiMethod;
import pro.kaleert.nyagram.api.objects.ChatInviteLink;
import lombok.*;

/**
 * Создает пригласительную ссылку для платной подписки на канал (за Telegram Stars).
 * <p>
 * Бот должен быть администратором канала с правом приглашать пользователей.
 * </p>
 *
 * @since 1.2.1
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateChatSubscriptionInviteLink extends BotApiMethod<ChatInviteLink> {

    public static final String PATH = "createChatSubscriptionInviteLink";

    @JsonProperty("chat_id")
    private String chatId;

    /** Название ссылки (0-32 символа). */
    @JsonProperty("name")
    private String name;

    /** Количество месяцев подписки (1-12). */
    @JsonProperty("subscription_period")
    private Integer subscriptionPeriod;

    /** Цена подписки в Telegram Stars за указанный период (1-2500). */
    @JsonProperty("subscription_price")
    private Integer subscriptionPrice;

    @Override
    public String getMethod() {
        return PATH;
    }

    @Override
    public ChatInviteLink deserializeResponse(String answer) throws TelegramApiRequestException {
        return deserializeResponse(answer, ChatInviteLink.class);
    }

    @Override
    public void validate() throws TelegramApiValidationException {
        if (chatId == null || chatId.isEmpty()) throw new TelegramApiValidationException("ChatId is required", PATH);
        if (subscriptionPeriod == null || subscriptionPeriod < 1 || subscriptionPeriod > 12) {
            throw new TelegramApiValidationException("Period must be 1-12 months", PATH);
        }
        if (subscriptionPrice == null || subscriptionPrice < 1 || subscriptionPrice > 2500) {
            throw new TelegramApiValidationException("Price must be 1-2500 Stars", PATH);
        }
    }
}