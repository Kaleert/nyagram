package pro.kaleert.nyagram.client.proxy;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

/**
 * Глобальный аутентификатор для SOCKS прокси с привязкой к ThreadLocal.
 * <p>
 * В Java SOCKS аутентификация работает только через глобальный Authenticator.
 * Этот класс гарантирует потокобезопасность при параллельных запросах через разные прокси.
 * </p>
 *
 * @since 1.1.5
 */
public class ProxyAuthenticator extends Authenticator {

    private static final ThreadLocal<PasswordAuthentication> CREDENTIALS = new ThreadLocal<>();
    private static boolean registered = false;

    public static synchronized void init() {
        if (!registered) {
            Authenticator.setDefault(new ProxyAuthenticator());
            registered = true;
        }
    }

    public static void setCredentials(String user, String pass) {
        CREDENTIALS.set(new PasswordAuthentication(user, pass.toCharArray()));
    }

    public static void clear() {
        CREDENTIALS.remove();
    }

    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
        if (getRequestorType() == RequestorType.PROXY) {
            return CREDENTIALS.get();
        }
        return super.getPasswordAuthentication();
    }
}