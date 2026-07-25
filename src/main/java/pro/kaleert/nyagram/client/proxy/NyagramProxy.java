package pro.kaleert.nyagram.client.proxy;

import java.net.InetSocketAddress;
import java.net.Proxy;

/**
 * Объект, представляющий параметры прокси-сервера.
 *
 * @since 1.1.5
 */
public record NyagramProxy(
        Proxy proxy,
        String username,
        String password
) {
    public static NyagramProxy http(String host, int port) {
        return new NyagramProxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(host, port)), null, null);
    }

    public static NyagramProxy http(String host, int port, String username, String password) {
        return new NyagramProxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(host, port)), username, password);
    }

    public static NyagramProxy socks(String host, int port) {
        return new NyagramProxy(new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(host, port)), null, null);
    }

    public static NyagramProxy socks(String host, int port, String username, String password) {
        return new NyagramProxy(new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(host, port)), username, password);
    }

    public static NyagramProxy noProxy() {
        return new NyagramProxy(Proxy.NO_PROXY, null, null);
    }
}