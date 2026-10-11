package pro.kaleert.nyagram.core.impl;

import pro.kaleert.nyagram.core.spi.BaseUrlController;
import pro.kaleert.nyagram.core.spi.NyagramBotConfig;
import lombok.RequiredArgsConstructor;

/**
 * Реализация по умолчанию, использующая статический URL из конфигурации.
 *
 * @since 1.2.0
 */
@RequiredArgsConstructor
public class DefaultBaseUrlController implements BaseUrlController {

    private final NyagramBotConfig config;

    @Override
    public String getBaseUrl() {
        String url = config.getApiUrl();
        return url.endsWith("/") ? url.substring(0, url.length() - 1) : url;
    }

    @Override
    public void reportError(String failedUrl, Throwable ex) {
    }
}