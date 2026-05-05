package com.kaleert.nyagram.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kaleert.nyagram.api.dto.TelegramResponse;
import com.kaleert.nyagram.api.exception.TelegramApiException;
import com.kaleert.nyagram.exception.TelegramNetworkException;
import com.kaleert.nyagram.api.meta.BotApiMethod;
import com.kaleert.nyagram.api.meta.MultipartRequest;
import com.kaleert.nyagram.api.objects.InputFile;
import com.kaleert.nyagram.api.objects.File;
import com.kaleert.nyagram.api.methods.GetFile;
import com.kaleert.nyagram.client.retry.ExponentialBackoffStrategy;
import com.kaleert.nyagram.core.spi.NyagramBotConfig;
import com.kaleert.nyagram.client.proxy.NyagramProxy;
import com.kaleert.nyagram.client.proxy.NyagramProxyProvider;
import com.kaleert.nyagram.client.proxy.ProxyContextHolder;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.util.MultiValueMap;
import org.springframework.util.LinkedMultiValueMap;

import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * HTTP-клиент для выполнения запросов к Telegram Bot API.
 * <p>
 * Обеспечивает синхронное и асинхронное выполнение методов, обработку ошибок,
 * автоматический повтор запросов (retries) и соблюдение ограничений по частоте (Rate Limiting).
 * </p>
 *
 * @since 1.0.0
 */
@Slf4j
@Component
public class NyagramClient {

    private final RestClient restClient;
    private final ObjectMapper objectMapper;
    @SuppressWarnings("unused")
    private final AdvancedMultipartBuilder multipartBuilder;
    private final Executor taskExecutor;
    private final TokenBucketRateLimiter rateLimiter;
    private final NyagramBotConfig botConfig;
    private final ExponentialBackoffStrategy backoffStrategy;
    private final ObjectProvider<NyagramProxyProvider> proxyProvider;
    private final Map<Class<?>, Boolean> multipartCache = new ConcurrentHashMap<>();

    private static final int MAX_RETRIES = 3;
    private static final long INITIAL_BACKOFF_MS = 500L;
    
    /**
     * Создает новый экземпляр клиента Nyagram.
     * <p>
     * Обычно этот конструктор вызывается автоматически Spring-контейнером.
     * </p>
     *
     * @param config Конфигурация бота.
     * @param objectMapper Jackson Mapper для сериализации/десериализации.
     * @param multipartBuilder Строитель multipart-запросов.
     * @param taskExecutor Пул потоков для асинхронных операций.
     * @param backoffStrategy Стратегия повторных попыток (backoff).
     */
    public NyagramClient(
            NyagramBotConfig config,
            ObjectMapper objectMapper,
            AdvancedMultipartBuilder multipartBuilder,
            @Qualifier("botTaskExecutor") Executor taskExecutor,
            ExponentialBackoffStrategy backoffStrategy,
            ObjectProvider<NyagramProxyProvider> proxyProvider
    ) {
        this.botConfig = config;
        this.objectMapper = objectMapper;
        this.multipartBuilder = multipartBuilder;
        this.taskExecutor = taskExecutor;
        this.backoffStrategy = backoffStrategy;
        this.proxyProvider = proxyProvider;
        this.rateLimiter = new TokenBucketRateLimiter(30, Duration.ofSeconds(1));

        String baseUrl = config.getApiUrl().endsWith("/") 
                ? config.getApiUrl() + "bot" + config.getBotToken() 
                : config.getApiUrl() + "/bot" + config.getBotToken();

        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }
    
    /**
     * Выполняет асинхронный запрос к Telegram API.
     * <p>
     * Запрос выполняется в пуле потоков {@code botTaskExecutor}.
     * </p>
     *
     * @param method Объект метода API.
     * @param <T> Тип возвращаемого значения.
     * @return Future, который будет завершен результатом запроса.
     */
    public <T extends Serializable> CompletableFuture<T> executeAsync(BotApiMethod<T> method) {
        return CompletableFuture.supplyAsync(() -> execute(method), taskExecutor);
    }
    
    /**
     * Выполняет синхронный запрос к Telegram API.
     * <p>
     * Метод автоматически обрабатывает сериализацию, отправку (JSON или Multipart)
     * и десериализацию ответа. Также включает логику повторных попыток (retries)
     * при ошибках сети и соблюдение лимитов скорости (Rate Limiting).
     * </p>
     *
     * @param method Объект метода API (например, {@link com.kaleert.nyagram.api.methods.send.SendMessage}).
     * @param <T> Тип возвращаемого значения (определяется методом).
     * @return Результат выполнения запроса (объект Telegram).
     * @throws com.kaleert.nyagram.api.exception.TelegramApiRequestException если API вернул ошибку.
     */
    public <T extends Serializable> T execute(BotApiMethod<T> method) {
        try {
            method.validate();
        } catch (Exception e) {
            throw new IllegalArgumentException("Validation failed for method " + method.getMethod() + ": " + e.getMessage(), e);
        }

        return executeWithRetry(method, 1, INITIAL_BACKOFF_MS);
    }

    private <T extends Serializable> T executeWithRetry(BotApiMethod<T> method, int attempt, long unusedBackoff) {
        rateLimiter.acquire();
        
        NyagramProxy currentProxy = null;
        if (proxyProvider.getIfAvailable() != null) {
            currentProxy = proxyProvider.getIfAvailable().getProxy();
            ProxyContextHolder.set(currentProxy);
        }
        
        try {
            return executeInternal(method);

        } catch (HttpClientErrorException e) {
            int code = e.getStatusCode().value();
        
            if (code == 429) {
                long retryAfter = parseRetryAfter(e);
                log.warn("⏳ Rate Limit (429) for {}. Sleeping {}s.", method.getMethod(), retryAfter);
                sleep(retryAfter * 1000L + 100);
                return executeWithRetry(method, attempt, 0);
            }
            
            if (code == 403) {
                log.info("⛔ User blocked the bot (403) during {}. Action skipped.", method.getMethod());
                return null;
            }
        
            log.error("❌ Client Error {} for {}: {}", code, method.getMethod(), e.getResponseBodyAsString());
            throw e;

        } catch (ResourceAccessException | HttpServerErrorException e) {
            if (proxyProvider.getIfAvailable() != null && currentProxy != null) {
                proxyProvider.getIfAvailable().onProxyError(currentProxy, e);
            }
            
            if (attempt >= MAX_RETRIES) {
                throw new TelegramNetworkException("Max retries exceeded", method.getMethod(), e);
            }
            
            long delay = backoffStrategy.nextBackoff(attempt);
            log.warn("🔄 Network error for {} (Attempt {}/{}). Retry in {}ms.", 
                     method.getMethod(), attempt, MAX_RETRIES, delay);
            
            sleep(delay);
            return executeWithRetry(method, attempt + 1, 0);
        } catch (Exception e) {
            log.error("❌ Unexpected error executing {}", method.getMethod(), e);
            throw new RuntimeException("Unexpected error in NyagramClient", e);
        } finally {
            ProxyContextHolder.clear();
        }
    }

    @SneakyThrows
    private <T extends Serializable> T executeInternal(BotApiMethod<T> method) {
        String methodName = method.getMethod();
        RestClient.RequestBodySpec requestSpec = restClient.post().uri("/" + methodName);
    
        if (method instanceof MultipartRequest multipartRequest) {
            MultiValueMap<String, Object> body = buildMultipart(method, multipartRequest.getFiles());
            requestSpec.contentType(MediaType.MULTIPART_FORM_DATA).body(body);
        } else {
            requestSpec.contentType(MediaType.APPLICATION_JSON).body(method);
        }

        String jsonResponse = requestSpec.retrieve().body(String.class);
        return method.deserializeResponse(jsonResponse);
    }

    private MultiValueMap<String, Object> buildMultipart(BotApiMethod<?> method, Map<String, InputFile> files) {
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        
        Map<String, Object> methodProps = objectMapper.convertValue(method, new TypeReference<Map<String, Object>>() {});
        methodProps.forEach((k, v) -> {
            if (!files.containsKey(k)) {
                 body.add(k, v instanceof String ? v : toJson(v));
            }
        });
    
        files.forEach((fieldName, file) -> {
            if (file.isNew()) {
                body.add(fieldName, new StreamingMultipartFile(file));
            } else {
                body.add(fieldName, file.getAttachName());
            }
        });
    
        return body;
    }

    @SneakyThrows
    private boolean requiresMultipart(BotApiMethod<?> method) {
        Boolean classPotential = multipartCache.computeIfAbsent(method.getClass(), clazz -> {
            Class<?> current = clazz;
            while (current != null) {
                for (Field field : current.getDeclaredFields()) {
                    if (InputFile.class.isAssignableFrom(field.getType()) || 
                        java.util.Collection.class.isAssignableFrom(field.getType())) { // Collection для SendMediaGroup
                        return true;
                    }
                }
                current = current.getSuperclass();
            }
            return false;
        });

        if (!classPotential) return false;

        if (method instanceof com.kaleert.nyagram.api.methods.send.SendMediaGroup) {
            return true; 
        }

        Class<?> current = method.getClass();
        while (current != null) {
            for (Field field : current.getDeclaredFields()) {
                if (InputFile.class.isAssignableFrom(field.getType())) {
                    field.setAccessible(true);
                    Object val = field.get(method);
                    if (val instanceof InputFile file && file.isNew()) {
                        return true;
                    }
                }
            }
            current = current.getSuperclass();
        }
        return false;
    }

    private void handleClientError(HttpClientErrorException e, BotApiMethod<?> method) {
        try {
            TelegramResponse<?> response = objectMapper.readValue(e.getResponseBodyAsString(), TelegramResponse.class);
            throw new TelegramApiException(
                    response.getDescription(),
                    method.getMethod(),
                    response.getErrorCode(),
                    response.getDescription()
            );
        } catch (JsonProcessingException ex) {
            throw new TelegramApiException("HTTP " + e.getStatusCode(), method.getMethod(), e.getStatusCode().value(), e.getResponseBodyAsString());
        }
    }

    private long parseRetryAfter(HttpClientErrorException e) {
        String val = e.getResponseHeaders() != null ? e.getResponseHeaders().getFirst("Retry-After") : null;
        if (val != null) {
            try { return Long.parseLong(val); } catch (NumberFormatException ignored) {}
        }
        return 5;
    }

    private void sleep(long ms) {
        try { TimeUnit.MILLISECONDS.sleep(ms); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }
    
    /**
     * Асинхронно получает информацию о файле по его идентификатору.
     * <p>
     * Использует метод {@link GetFile}. Результат содержит {@code file_path},
     * необходимый для скачивания файла.
     * </p>
     *
     * @param fileId Уникальный идентификатор файла.
     * @return Future с объектом {@link File}.
     */
    public CompletableFuture<File> getFileAsync(String fileId) {
        return executeAsync(new GetFile(fileId));
    }
    
    /**
     * Скачивает файл с серверов Telegram по его пути.
     * <p>
     * Путь к файлу можно получить, вызвав {@link com.kaleert.nyagram.api.methods.GetFile}.
     * </p>
     *
     * @param filePath Путь к файлу (например, "photos/file_0.jpg").
     * @return InputStream с содержимым файла.
     */
    public InputStream downloadFileAsStream(String filePath) {
        String downloadUrl = "https://api.telegram.org/file/bot" + botConfig.getBotToken() + "/" + filePath;
        return restClient.get().uri(downloadUrl).retrieve().body(InputStream.class);
    }
    
    /**
     * Реализация алгоритма Token Bucket для ограничения частоты запросов (Rate Limiting).
     * <p>
     * Гарантирует, что бот не превысит глобальные лимиты Telegram API (около 30 запросов в секунду).
     * Блокирует поток, если лимит исчерпан, до появления новых токенов.
     * </p>
     */
    private static class TokenBucketRateLimiter {
        private final long capacity;
        private final long refillPeriodNanos;
        private final AtomicLong tokens;
        private final AtomicLong lastRefillNanos;
        
        /**
         * Инициализирует ограничитель скорости.
         *
         * @param capacity Максимальное количество токенов в корзине (burst capacity).
         * @param refillPeriod Период пополнения токенов.
         */
        public TokenBucketRateLimiter(long capacity, Duration refillPeriod) {
            this.capacity = capacity;
            this.refillPeriodNanos = refillPeriod.toNanos();
            this.tokens = new AtomicLong(capacity);
            this.lastRefillNanos = new AtomicLong(System.nanoTime());
        }
        
        /**
         * Пытается получить токен из корзины.
         * <p>
         * Если токенов нет, поток блокируется и ожидает их появления.
         * Гарантирует, что частота выполнения не превысит заданный лимит.
         * </p>
         */
        public void acquire() {
            while (true) {
                refill();
                long currentTokens = tokens.get();
                if (currentTokens > 0) {
                    if (tokens.compareAndSet(currentTokens, currentTokens - 1)) return;
                } else {
                    try {
                        long sleepNs = refillPeriodNanos / capacity;
                        TimeUnit.NANOSECONDS.sleep(Math.max(1_000_000, sleepNs)); 
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        throw new RuntimeException("Interrupted", e);
                    }
                }
            }
        }

        private void refill() {
            long now = System.nanoTime();
            long last = lastRefillNanos.get();
            long duration = now - last;
            if (duration > refillPeriodNanos) {
                if (lastRefillNanos.compareAndSet(last, now)) {
                    tokens.set(capacity);
                }
            }
        }
    }
    
    @SneakyThrows
    private String toJson(Object value) {
        return objectMapper.writeValueAsString(value);
    }
}