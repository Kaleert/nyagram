package com.kaleert.nyagram.core.impl;

import com.kaleert.nyagram.core.spi.BaseUrlController;
import com.kaleert.nyagram.core.spi.NyagramBotConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

/**
 * Реализация по умолчанию, использующая статический URL из конфигурации.
 *
 * @since 1.2.0
 */
@Component
@ConditionalOnMissingBean(BaseUrlController.class)
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