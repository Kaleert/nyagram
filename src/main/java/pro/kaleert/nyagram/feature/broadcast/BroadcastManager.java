package pro.kaleert.nyagram.feature.broadcast;

import pro.kaleert.nyagram.api.exception.TelegramApiException;
import pro.kaleert.nyagram.api.methods.send.SendMessage;
import pro.kaleert.nyagram.client.NyagramClient;
import pro.kaleert.nyagram.api.objects.replykeyboard.ReplyKeyboard;
import pro.kaleert.nyagram.api.methods.send.CopyMessage;
import pro.kaleert.nyagram.core.concurrency.NyagramExecutor;
import pro.kaleert.nyagram.feature.broadcast.event.BroadcastEvents.BroadcastCompleteEvent;
import pro.kaleert.nyagram.feature.broadcast.event.BroadcastEvents.UserBlockedEvent;
import pro.kaleert.nyagram.feature.broadcast.spi.BroadcastTargetProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.concurrent.Phaser;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

/**
 * Сервис для управления массовыми рассылками сообщений.
 * <p>
 * Обеспечивает:
 * <ul>
 *     <li>Многопоточную отправку (использует {@link NyagramExecutor}).</li>
 *     <li>Соблюдение лимитов API (Throttling) для предотвращения ошибок 429.</li>
 *     <li>Обработку ошибок (например, блокировка бота пользователем).</li>
 *     <li>Публикацию событий о завершении рассылки.</li>
 * </ul>
 * </p>
 *
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BroadcastManager {

    private final NyagramClient client;
    private final NyagramExecutor taskExecutor;
    private final BroadcastTargetProvider targetProvider;
    private final ApplicationEventPublisher eventPublisher;
    
    /**
     * Запускает процесс рассылки в фоновом режиме.
     * <p>
     * Метод возвращает управление немедленно. Статус рассылки можно отслеживать через события.
     * </p>
     *
     * @param text Текст сообщения (поддерживает HTML).
     * @param senderAdminId ID администратора, запустившего рассылку (для логов).
     */
    public void broadcast(String text, Integer senderAdminId) {
        log.info("Starting broadcast initiated by admin {}", senderAdminId);
        new Thread(() -> runBroadcastLoop(text), "broadcast-manager").start();
    }
    
    /**
     * @param text Текст рассылки
     *
     * Старый внутренний цикл рассылки.
     * Перебирает получателей из {@link BroadcastTargetProvider} и ставит задачи в пул потоков.
     * Оставлен для обратной совместимости
     */
     @Deprecated(since = "1.1.4", forRemoval = true)
    private void runBroadcastLoop(String text) {
        runTextBroadcastLoop(text);
    }
    
    /**
     * Новый внутренний цикл рассылки.
     * Перебирает получателей из {@link BroadcastTargetProvider} и ставит задачи в пул потоков.
     */
    private void runTextBroadcastLoop(String text) {
        long startTime = System.currentTimeMillis();
        
        AtomicLong total = new AtomicLong();
        AtomicLong success = new AtomicLong();
        AtomicLong failed = new AtomicLong();
        
        Phaser phaser = new Phaser(1); 
    
        try (Stream<Long> targets = targetProvider.getTargetChatIds()) {
            
            targets.forEach(chatId -> {
                total.incrementAndGet();
                phaser.register();
    
                try {
                    taskExecutor.execute(chatId, () -> {
                        try {
                            client.execute(SendMessage.builder()
                                    .chatId(chatId.toString())
                                    .text(text)
                                    .parseMode("HTML")
                                    .build());
                            success.incrementAndGet();
                        } catch (TelegramApiException e) {
                            handleError(e, chatId);
                            failed.incrementAndGet();
                        } catch (Exception e) {
                            log.error("Generic error sending to {}", chatId, e);
                            failed.incrementAndGet();
                        } finally {
                            phaser.arriveAndDeregister();
                        }
                    });
                } catch (Exception e) {
                    phaser.arriveAndDeregister();
                    failed.incrementAndGet();
                    log.error("Failed to submit broadcast task for {}", chatId, e);
                }
    
                throttle(total.get());
            });
            
        } catch (Exception e) {
            log.error("Error during broadcast iteration", e);
        }
    
        log.info("All broadcast tasks submitted. Waiting for completion...");
    
        phaser.arriveAndAwaitAdvance();
    
        long duration = System.currentTimeMillis() - startTime;
        log.info("Broadcast finished. Duration: {}ms. Total: {}, Success: {}, Failed: {}", 
                duration, total.get(), success.get(), failed.get());
        
        eventPublisher.publishEvent(new BroadcastCompleteEvent(
            total.get(), success.get(), failed.get(), duration, null
        ));
    }
    
    private void handleError(TelegramApiException e, Long chatId) {
        if (e.getErrorCode() != null && e.getErrorCode() == 403) {
            eventPublisher.publishEvent(new UserBlockedEvent(chatId));
        } else {
            log.warn("Failed to send broadcast to {}: {}", chatId, e.getMessage());
        }
    }

    private void throttle(long count) {
        try { 
            Thread.sleep(40); 
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * Запускает продвинутую рассылку любого сообщения через CopyMessage.
     *
     * @param fromChatId ID чата, в котором находится исходное сообщение (например, админский чат).
     * @param messageId ID исходного сообщения, которое нужно разослать.
     * @param replyMarkup Клавиатура, которую нужно прикрепить к рассылке (опционально).
     * @param senderAdminId ID администратора, запустившего рассылку.
     * 
     * @since 1.1.4
     */
    public void broadcastMessage(Long fromChatId, Integer messageId, ReplyKeyboard replyMarkup, Integer senderAdminId) {
        log.info("Starting advanced broadcast initiated by admin {}", senderAdminId);
        new Thread(() -> runAdvancedBroadcastLoop(fromChatId, messageId, replyMarkup, senderAdminId), "broadcast-manager").start();
    }
    /**
    * @param fromChatId Айди чата, из которого взято сообщение
    * @param messageId Айди сообщения
    * @param replyMarkup Клавиатура, которую нужно прикрепить к рассылке (опционально).
    * 
    * Новый внутренний метод рассылки, поддерживает любые типы сообщений
    *
    * @since 1.1.4
    */
    @Deprecated(since = "1.1.5", forRemoval = true)
    private void runCopyBroadcastLoop(Long fromChatId, Integer messageId, ReplyKeyboard replyMarkup, Integer senderAdminId) {
        long startTime = System.currentTimeMillis();
        java.util.concurrent.atomic.AtomicLong total = new java.util.concurrent.atomic.AtomicLong();
        java.util.concurrent.atomic.AtomicLong success = new java.util.concurrent.atomic.AtomicLong();
        java.util.concurrent.atomic.AtomicLong failed = new java.util.concurrent.atomic.AtomicLong();
        
        java.util.concurrent.Phaser phaser = new java.util.concurrent.Phaser(1); 
    
        try (java.util.stream.Stream<Long> targets = targetProvider.getTargetChatIds()) {
            targets.forEach(chatId -> {
                total.incrementAndGet();
                phaser.register();
    
                try {
                    taskExecutor.execute(chatId, () -> {
                        try {
                            client.execute(pro.kaleert.nyagram.api.methods.send.CopyMessage.builder()
                                    .chatId(chatId.toString())
                                    .fromChatId(fromChatId.toString())
                                    .messageId(messageId)
                                    .replyMarkup(replyMarkup)
                                    .build());
                            success.incrementAndGet();
                        } catch (pro.kaleert.nyagram.api.exception.TelegramApiException e) {
                            handleError(e, chatId);
                            failed.incrementAndGet();
                        } catch (Exception e) {
                            log.error("Generic error copying to {}", chatId, e);
                            failed.incrementAndGet();
                        } finally {
                            phaser.arriveAndDeregister();
                        }
                    });
                } catch (Exception e) {
                    phaser.arriveAndDeregister();
                    failed.incrementAndGet();
                    log.error("Failed to submit copy broadcast task for {}", chatId, e);
                }
    
                throttle(total.get());
            });
            
        } catch (Exception e) {
            log.error("Error during copy broadcast iteration", e);
        }
    
        finishBroadcast(startTime, phaser, total.get(), success, failed, senderAdminId);
    }
    
    /**
     * Ожидает завершения всех запущенных задач рассылки, логирует результаты
     * и публикует событие {@link BroadcastCompleteEvent}.
     * <p>
     * Использование {@code phaser.arriveAndAwaitAdvance()} гарантирует, что 
     * управляющий поток (имеющий изначальную регистрацию "1") заблокируется 
     * до тех пор, пока все отправленные в пул потоков задачи не вызовут {@code arriveAndDeregister()}.
     * </p>
     *
     * @param startTime Время начала рассылки в миллисекундах.
     * @param phaser    Объект синхронизации, управляющий потоками рассылки.
     * @param total     Общее количество пользователей, которым пытались отправить сообщение.
     * @param success   Количество успешных отправок.
     * @param failed    Количество неудачных отправок (ошибки, блокировки).
     * @param senderAdminId ID администратора, запустившего рассылку (для логов и событий).
     * 
     * @since 1.1.4
     */
    private void finishBroadcast(long startTime, Phaser phaser, long total, 
                                java.util.concurrent.atomic.AtomicLong success, 
                                java.util.concurrent.atomic.AtomicLong failed, 
                                Integer senderAdminId) {
        log.info("All broadcast tasks submitted. Waiting for completion...");
        phaser.arriveAndAwaitAdvance();
        
        long s = success.get();
        long f = failed.get();
        long duration = System.currentTimeMillis() - startTime;
        
        log.info("Broadcast finished. Duration: {}ms. Total: {}, Success: {}, Failed: {}", 
                duration, total, s, f);
        
        eventPublisher.publishEvent(new BroadcastCompleteEvent(total, s, f, duration, senderAdminId));
    }
    
    /**
     * Внутренний цикл продвинутой массовой рассылки сообщений.
     * <p>
     * Осуществляет многопоточную отправку существующего сообщения (поддерживаются любые медиаформаты).
     * Использует гибридный механизм отправки:
     * <ul>
     *     <li>Если {@code replyMarkup} <b>не задан</b>, используется метод {@link pro.kaleert.nyagram.api.methods.send.ForwardMessage}
     *     для максимальной скорости и сохранения оригинального вида поста.</li>
     *     <li>Если {@code replyMarkup} <b>задан</b>, используется метод {@link pro.kaleert.nyagram.api.methods.send.CopyMessage},
     *     позволяющий прикрепить новые кнопки к рассылаемому контенту.</li>
     * </ul>
     * Задачи распределяются через {@link pro.kaleert.nyagram.core.concurrency.NyagramExecutor}, а ожидание завершения всех потоков 
     * синхронизируется с помощью {@link java.util.concurrent.Phaser}.
     * </p>
     *
     * @param fromChatId    ID чата (например, админской группы), из которого берется исходное сообщение.
     * @param messageId     ID исходного сообщения для рассылки.
     * @param replyMarkup   Новая Inline-клавиатура, которую необходимо прикрепить к копии сообщения (может быть {@code null}).
     * @param senderAdminId ID администратора, запустившего рассылку (для логов и событий завершения).
     *
     * @since 1.1.5
     */
    private void runAdvancedBroadcastLoop(Long fromChatId, Integer messageId, ReplyKeyboard replyMarkup, Integer senderAdminId) {
        long startTime = System.currentTimeMillis();
        java.util.concurrent.atomic.AtomicLong total = new java.util.concurrent.atomic.AtomicLong();
        java.util.concurrent.atomic.AtomicLong success = new java.util.concurrent.atomic.AtomicLong();
        java.util.concurrent.atomic.AtomicLong failed = new java.util.concurrent.atomic.AtomicLong();
        
        java.util.concurrent.Phaser phaser = new java.util.concurrent.Phaser(1); 
    
        try (java.util.stream.Stream<Long> targets = targetProvider.getTargetChatIds()) {
            targets.forEach(chatId -> {
                total.incrementAndGet();
                phaser.register();
    
                try {
                    taskExecutor.execute(chatId, () -> {
                        try {
                            if (replyMarkup == null) {
                                client.execute(pro.kaleert.nyagram.api.methods.send.ForwardMessage.builder()
                                        .chatId(chatId.toString())
                                        .fromChatId(fromChatId.toString())
                                        .messageId(messageId)
                                        .build());
                            } else {
                                client.execute(pro.kaleert.nyagram.api.methods.send.CopyMessage.builder()
                                        .chatId(chatId.toString())
                                        .fromChatId(fromChatId.toString())
                                        .messageId(messageId)
                                        .replyMarkup(replyMarkup)
                                        .build());
                            }
                            success.incrementAndGet();
                        } catch (pro.kaleert.nyagram.api.exception.TelegramApiException e) {
                            handleError(e, chatId);
                            failed.incrementAndGet();
                        } catch (Exception e) {
                            log.error("Generic error sending to {}", chatId, e);
                            failed.incrementAndGet();
                        } finally {
                            phaser.arriveAndDeregister();
                        }
                    });
                } catch (Exception e) {
                    phaser.arriveAndDeregister();
                    failed.incrementAndGet();
                    log.error("Failed to submit broadcast task for {}", chatId, e);
                }
    
                throttle(total.get());
            });
            
        } catch (Exception e) {
            log.error("Error during broadcast iteration", e);
        }
    
        finishBroadcast(startTime, phaser, total.get(), success, failed, senderAdminId);
    }
}