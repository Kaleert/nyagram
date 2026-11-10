package pro.kaleert.nyagram.api.objects.widget;

import com.fasterxml.jackson.annotation.*;
import pro.kaleert.nyagram.api.meta.BotApiObject;
import pro.kaleert.nyagram.api.objects.User;
import pro.kaleert.nyagram.api.objects.chat.Chat;

/**
 * Объект Telegram Bot API 10.4: widget query wallet.
 * @param address Адрес.
 * @param amount Количество или сумма согласно Telegram API.
 * @param publicKey Поле JSON {@code public_key} Telegram Bot API.
 * @param stateInit Поле JSON {@code state_init} Telegram Bot API.
 * @since 1.2.4
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record WidgetQueryWallet(
    @JsonProperty("address") String address,
    @JsonProperty("amount") String amount,
    @JsonProperty("public_key") String publicKey,
    @JsonProperty("state_init") String stateInit
) implements BotApiObject {}
