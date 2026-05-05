package com.kaleert.nyagram.client.proxy;

/**
 * Интерфейс для динамического предоставления и ротации прокси.
 * <p>
 * Вы должны реализовать этот интерфейс и объявить его как Spring Bean, 
 * если хотите использовать прокси в боте. Метод {@code getProxy()} будет 
 * вызываться ПЕРЕД каждым обращением к API Telegram.
 * </p>
 *
 * @since 1.1.5
 */
public interface NyagramProxyProvider {

    /**
     * Возвращает прокси для текущего запроса.
     * @return Объект NyagramProxy (возвращайте NyagramProxy.noProxy(), если хотите выполнить запрос напрямую).
     */
    NyagramProxy getProxy();

    /**
     * Callback, который вызывается фреймворком, если при использовании прокси произошла ошибка сети
     * (Таймаут, сброс соединения и т.д.). Идеальное место, чтобы удалить прокси из пула.
     *
     * @param proxy Прокси, вызвавший ошибку.
     * @param ex Ошибка сети.
     */
    default void onProxyError(NyagramProxy proxy, Throwable ex) {}
}