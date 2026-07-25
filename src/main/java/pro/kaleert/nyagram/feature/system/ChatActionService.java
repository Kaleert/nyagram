package pro.kaleert.nyagram.feature.system;

import pro.kaleert.nyagram.api.methods.send.SendChatAction;
import pro.kaleert.nyagram.client.NyagramClient;
import pro.kaleert.nyagram.api.objects.ChatAction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import jakarta.annotation.PreDestroy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * Сервис для управления статусами чата (Chat Action).
 * <p>
 * Позволяет отправлять статусы "печатает..." (typing), "загружает фото..." и т.д.
 * Также умеет поддерживать статус активным в течение длительного времени,
 * автоматически обновляя его каждые 4-5 секунд.
 * </p>
 *
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ChatActionService {

    private final NyagramClient client;
    private final ConcurrentHashMap<Long, ActiveAction> activeActions = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2, r -> {
        Thread thread = new Thread(r, "nyagram-chat-action");
        thread.setDaemon(true);
        return thread;
    });
    
    /**
     * Отправляет однократный статус действия.
     * Статус виден пользователю около 5 секунд.
     *
     * @param chatId ID чата.
     * @param action Тип действия (например, TYPING).
     */
    public void send(Long chatId, ChatAction action) {
        try {
            SendChatAction msg = SendChatAction.builder()
                    .chatId(chatId.toString())
                    .action(action.getValue())
                    .build();
            client.execute(msg);
        } catch (Exception e) {
            log.warn("Failed to send chat action '{}' to {}: {}", action, chatId, e.getMessage());
        }
    }
    
    /**
     * Отправляет статус "печатает..." (TYPING).
     * <p>
     * Сообщает пользователю, что бот готовит текстовый ответ.
     * Статус исчезает автоматически через 5 секунд или при отправке сообщения.
     * </p>
     *
     * @param chatId Уникальный идентификатор чата.
     */
    public void typing(Long chatId) {
        send(chatId, ChatAction.TYPING);
    }
    
    /**
     * Отправляет статус "загружает фото..." (UPLOAD_PHOTO).
     *
     * @param chatId ID чата.
     */
    public void uploadPhoto(Long chatId) {
        send(chatId, ChatAction.UPLOAD_PHOTO);
    }
    
    /**
     * Отправляет статус "записывает голосовое..." (RECORD_VOICE).
     *
     * @param chatId ID чата.
     */
    public void recordVoice(Long chatId) {
        send(chatId, ChatAction.RECORD_VOICE);
    }

    public void start(Long chatId, ChatAction action) {
        activeActions.compute(chatId, (id, current) -> {
            if (current != null && current.action().equals(action) && !current.future().isCancelled()) {
                return current;
            }
            if (current != null) {
                current.future().cancel(true);
            }

            ScheduledFuture<?> future = scheduler.scheduleAtFixedRate(
                    () -> send(chatId, action),
                    0,
                    4,
                    TimeUnit.SECONDS
            );
            return new ActiveAction(action, future);
        });
    }

    public void stop(Long chatId) {
        ActiveAction action = activeActions.remove(chatId);
        if (action != null) {
            action.future().cancel(true);
        }
    }
    
    /**
     * Выполняет задачу, поддерживая статус "печатает..." (typing) активным.
     * <p>
     * Автоматически обновляет статус каждые несколько секунд, пока {@code task} выполняется.
     * </p>
     *
     * @param chatId ID чата.
     * @param task Задача, которую нужно выполнить.
     * @param <T> Тип результата задачи.
     * @return Future с результатом задачи.
     */
    @Async
    public <T> CompletableFuture<T> executeWithTyping(Long chatId, Supplier<T> task) {
        return executeWithAction(chatId, ChatAction.TYPING, task);
    }
    
    /**
     * Выполняет длительную задачу, поддерживая указанный статус действия активным.
     * <p>
     * Запускает фоновый поток, который периодически отправляет {@code sendChatAction},
     * пока задача {@code task} не завершится.
     * </p>
     *
     * @param chatId ID чата.
     * @param action Тип действия (например, "typing", "upload_photo", "record_voice").
     * @param task Задача (Supplier), которую нужно выполнить.
     * @param <T> Тип возвращаемого значения задачи.
     * @return Future с результатом выполнения задачи.
     */
    public <T> CompletableFuture<T> executeWithAction(Long chatId, ChatAction action, Supplier<T> task) {
        return CompletableFuture.supplyAsync(() -> {
            start(chatId, action);

            try {
                return task.get();
            } finally {
                stop(chatId);
            }
        });
    }

    @PreDestroy
    public void shutdown() {
        activeActions.values().forEach(action -> action.future().cancel(true));
        activeActions.clear();
        scheduler.shutdownNow();
    }

    private record ActiveAction(ChatAction action, ScheduledFuture<?> future) {
    }
}
