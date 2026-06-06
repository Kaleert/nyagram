package com.kaleert.nyagram.core.spi;

/**
 * Контроллер для динамического управления базовым URL Telegram Bot API.
 * Позволяет переключать прокси-серверы или API-шлюзы "на лету" в случае сбоев.
 *
 * @since 1.2.0
 */
public interface BaseUrlController {

    /**
     * Возвращает текущий активный базовый URL.
     *
     * @return URL (например, "https://api.telegram.org").
     */
    String getBaseUrl();

    /**
     * Вызывается фреймворком, если при выполнении запроса по текущему URL произошла сетевая ошибка.
     *
     * @param failedUrl URL, по которому произошла ошибка.
     * @param ex Исключение, вызвавшее сбой.
     */
    void reportError(String failedUrl, Throwable ex);
}