package pro.kaleert.nyagram.core.concurrency;

import pro.kaleert.nyagram.api.objects.Update;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.ObjectProvider;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Ручная стратегия конкурентности.
 * <p>
 * Просто передает задачи в {@link java.util.concurrent.Executor} без какой-либо гарантии порядка.
 * Активируется свойством {@code nyagram.concurrency.mode=manual}.
 * Подходит для stateless-ботов, где порядок сообщений не важен, но важна максимальная скорость.
 * </p>
 *
 * @since 1.0.0
 */
@Slf4j
@Component
@ConditionalOnProperty(name = "nyagram.concurrency.mode", havingValue = "manual")
public class ManualConcurrencyStrategy implements BotConcurrencyStrategy {

    private final Executor delegate;
    
    /**
     * Инициализирует стратегию ручной конкурентности.
     * <p>
     * Пытается найти бин {@code nyagramExecutor}. Если он не найден, создает
     * стандартный {@link Executors#newCachedThreadPool()}.
     * </p>
     *
     * @param executorProvider Провайдер бина экзекьютора.
     */
    public ManualConcurrencyStrategy(@Qualifier("nyagramExecutor") ObjectProvider<Executor> executorProvider) {
        this.delegate = executorProvider.getIfAvailable(() -> {
            log.info("🛠 Initializing MANUAL Concurrency Strategy with default CachedThreadPool.");
            log.warn("⚠️ Warning: No 'nyagramExecutor' bean found. Defaulting to unbounded thread pool.");
            return Executors.newCachedThreadPool();
        });
        
        if (executorProvider.getIfAvailable() != null) {
             log.info("🛠 Initializing MANUAL Concurrency Strategy with custom executor: {}", this.delegate.getClass().getName());
        }
    }

    @Override
    public void execute(Update update, Runnable task) {
        delegate.execute(task);
    }
}