package com.kaleert.nyagram.client.proxy;

/**
 * ThreadLocal хранилище для передачи прокси из клиента в фабрику HTTP-запросов.
 *
 * @since 1.1.5
 */
public class ProxyContextHolder {
    private static final ThreadLocal<NyagramProxy> HOLDER = new ThreadLocal<>();

    public static void set(NyagramProxy proxy) {
        HOLDER.set(proxy);
    }

    public static NyagramProxy get() {
        return HOLDER.get();
    }

    public static void clear() {
        HOLDER.remove();
    }
}