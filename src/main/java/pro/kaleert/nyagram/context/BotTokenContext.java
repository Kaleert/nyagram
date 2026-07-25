package pro.kaleert.nyagram.context;

import lombok.experimental.UtilityClass;

/**
 * Хранилище токена бота для текущего потока.
 * <p>
 * Позволяет фреймворку работать в режиме Multi-Bot. Если токен установлен,
 * {@link pro.kaleert.nyagram.client.NyagramClient} будет использовать его
 * вместо дефолтного токена из конфигурации.
 * </p>
 *
 * @since 1.2.1
 */
@UtilityClass
public class BotTokenContext {

    private static final ThreadLocal<String> TOKEN_HOLDER = new ThreadLocal<>();
    private static final ThreadLocal<String> USERNAME_HOLDER = new ThreadLocal<>();

    public static void setToken(String token) {
        TOKEN_HOLDER.set(token);
    }

    public static String getToken() {
        return TOKEN_HOLDER.get();
    }

    public static void setUsername(String username) {
        USERNAME_HOLDER.set(username);
    }

    public static String getUsername() {
        return USERNAME_HOLDER.get();
    }

    public static void clear() {
        TOKEN_HOLDER.remove();
        USERNAME_HOLDER.remove();
    }
}