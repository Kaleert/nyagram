package com.kaleert.nyagram.feature.files;

import com.kaleert.nyagram.client.NyagramClient;
import com.kaleert.nyagram.client.proxy.DynamicProxyClientHttpRequestFactory;
import com.kaleert.nyagram.client.proxy.NyagramProxy;
import com.kaleert.nyagram.client.proxy.NyagramProxyProvider;
import com.kaleert.nyagram.client.proxy.ProxyAuthenticator;
import com.kaleert.nyagram.client.proxy.ProxyContextHolder;
import com.kaleert.nyagram.core.spi.NyagramBotConfig;
import com.kaleert.nyagram.core.spi.BaseUrlController;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

/**
 * Сервис для работы с файлами Telegram.
 * <p>
 * Упрощает процесс скачивания файлов:
 * 1. Получает объект {@link com.kaleert.nyagram.api.objects.File} по {@code file_id}.
 * 2. Формирует ссылку для скачивания.
 * 3. Загружает поток байтов и сохраняет его на диск.
 * </p>
 *
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {

    private final NyagramClient client;
    private final NyagramBotConfig config;
    private final ObjectProvider<NyagramProxyProvider> proxyProvider;
    private final BaseUrlController baseUrlController;
    
    private RestClient rawClient;

    @PostConstruct
    public void init() {
        RestClient.Builder builder = RestClient.builder();
        if (proxyProvider.getIfAvailable() != null) {
            ProxyAuthenticator.init();
            builder.requestFactory(new DynamicProxyClientHttpRequestFactory());
        }
        this.rawClient = builder.build();
    }

    /**
     * Асинхронно скачивает файл по его ID и сохраняет в указанное место.
     *
     * @param fileId Идентификатор файла в Telegram.
     * @param destination Путь на локальном диске, куда сохранить файл.
     * @return Future, который завершится путем к сохраненному файлу.
     */
    public CompletableFuture<Path> downloadFile(String fileId, Path destination) {
        return client.getFileAsync(fileId)
                .thenApply(file -> downloadContent(file.filePath(), destination));
    }
    
    /**
     * Скачивает содержимое файла по известному {@code file_path} (из объекта File).
     *
     * @param telegramFilePath Путь файла на сервере Telegram.
     * @param destination Локальный путь назначения.
     * @return Локальный путь к сохраненному файлу.
     * @throws RuntimeException если скачивание не удалось.
     */
    public Path downloadContent(String telegramFilePath, Path destination) {
        String url = String.format("%s/file/bot%s/%s", 
                baseUrlController.getBaseUrl(), config.getBotToken(), telegramFilePath);
        
        NyagramProxy currentProxy = null;
        if (proxyProvider.getIfAvailable() != null) {
            currentProxy = proxyProvider.getIfAvailable().getProxy();
            ProxyContextHolder.set(currentProxy);
        }

        try {
            byte[] body = rawClient.get()
                    .uri(url)
                    .retrieve()
                    .body(byte[].class);

            if (body == null || body.length == 0) {
                throw new RuntimeException("Response body is empty");
            }

            Files.createDirectories(destination.getParent());
            Files.write(destination, body);
            
            return destination;

        } catch (Exception e) {
            if (proxyProvider.getIfAvailable() != null && currentProxy != null) {
                proxyProvider.getIfAvailable().onProxyError(currentProxy, e);
            }
            log.error("Failed to download file {}", telegramFilePath, e);
            throw new RuntimeException("Download failed", e);
        } finally {
            ProxyContextHolder.clear();
        }
    }
}