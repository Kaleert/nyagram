package com.kaleert.nyagram.core;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kaleert.nyagram.config.NyagramProperties;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.InputStream;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "nyagram.check-updates", havingValue = "true", matchIfMissing = true)
public class NyagramUpdateChecker {

    private final ObjectMapper mapper;
    private final NyagramProperties properties;
    private final RestTemplate restTemplate = new RestTemplate();
    
    private static final String GITHUB_PACKAGE_URL = "https://raw.githubusercontent.com/Kaleert/nyagram/master/package.json";

    @PostConstruct
    public void checkForUpdates() {
        Thread checkerThread = new Thread(() -> {
            try {
                // 1. Читаем локальную версию из ресурсов (куда ее положил Gradle)
                InputStream is = getClass().getClassLoader().getResourceAsStream("package.json");
                if (is == null) return;
                
                JsonNode localPkg = mapper.readTree(is);
                String localVersion = localPkg.get("version").asText();
                String apiVersion = localPkg.get("telegramApiVersion").asText();

                log.info("😺 Nyagram Framework v{} (Telegram Bot API {})", localVersion, apiVersion);

                // 2. Запрашиваем удаленную версию с GitHub
                String remoteJson = restTemplate.getForObject(GITHUB_PACKAGE_URL, String.class);
                if (remoteJson == null) return;

                JsonNode remotePkg = mapper.readTree(remoteJson);
                String remoteVersion = remotePkg.get("version").asText();
                String docsUrl = remotePkg.has("docsUrl") ? remotePkg.get("docsUrl").asText() : "https://nyagram.kaleert.pro";

                // 3. Сравниваем версии
                if (isNewerVersion(localVersion, remoteVersion)) {
                    log.warn("=========================================================");
                    log.warn("✨ Доступна новая версия Nyagram: {} (Ваша версия: {})", remoteVersion, localVersion);
                    log.warn("👉 Changelog: {}/version/{}", docsUrl, remoteVersion);
                    log.warn("=========================================================");
                }

            } catch (Exception e) {
                log.debug("Failed to check for Nyagram updates: {}", e.getMessage());
            }
        }, "nyagram-update-checker");
        
        checkerThread.setDaemon(true);
        checkerThread.start();
    }

    private boolean isNewerVersion(String local, String remote) {
        String[] localParts = local.split("\\.");
        String[] remoteParts = remote.split("\\.");
        
        int length = Math.max(localParts.length, remoteParts.length);
        for (int i = 0; i < length; i++) {
            int l = i < localParts.length ? Integer.parseInt(localParts[i].replaceAll("[^0-9]", "")) : 0;
            int r = i < remoteParts.length ? Integer.parseInt(remoteParts[i].replaceAll("[^0-9]", "")) : 0;
            if (r > l) return true;
            if (l > r) return false;
        }
        return false;
    }
}