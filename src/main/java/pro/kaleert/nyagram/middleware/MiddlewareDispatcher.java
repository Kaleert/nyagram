package pro.kaleert.nyagram.middleware;

import pro.kaleert.nyagram.command.CommandContext;
import pro.kaleert.nyagram.meta.CommandMeta;
import pro.kaleert.nyagram.middleware.annotation.MiddlewareRoute;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * Диспетчер Middleware.
 * <p>
 * Собирает все бины {@link Middleware} из контекста Spring, сортирует их согласно {@link org.springframework.core.Ordered}
 * и запускает выполнение цепочки для каждой входящей команды.
 * </p>
 *
 * @since 1.0.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MiddlewareDispatcher {

    private final List<Middleware> middlewares;
    private List<Middleware> sortedMiddlewares;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();
    
    /**
     * Инициализирует диспетчер.
     * <p>
     * Сортирует внедренный список middleware в соответствии с приоритетом {@code @Order}.
     * </p>
     */
    @PostConstruct
    public void init() {
        sortedMiddlewares = middlewares.stream()
                .sorted(Comparator.comparing(Middleware::getOrder))
                .toList();
        
        log.info("Initialized MiddlewareDispatcher with {} middlewares", sortedMiddlewares.size());
    }
    
    /**
     * Запускает выполнение цепочки Middleware для команды.
     *
     * @param context Контекст команды.
     * @param meta Метаданные команды.
     * @return Future с результатом выполнения цепочки (Continue, Stop или Error).
     */
    public CompletableFuture<MiddlewareResult> dispatch(CommandContext context, CommandMeta meta) {
        List<Middleware> applicableMiddlewares = sortedMiddlewares.stream()
                .filter(m -> isApplicable(m, meta.getFullCommandPath()))
                .collect(Collectors.toList());

        if (applicableMiddlewares.isEmpty()) {
            return new MiddlewareChain(applicableMiddlewares, context, meta).proceed();
        }

        return new MiddlewareChain(applicableMiddlewares, context, meta).proceed();
    }
    
    private boolean isApplicable(Middleware middleware, String commandPath) {
        MiddlewareRoute route = middleware.getClass().getAnnotation(MiddlewareRoute.class);
        if (route == null || route.value().length == 0) {
            return true;
        }
        
        String pathToCheck = commandPath != null ? commandPath : "";
        for (String pattern : route.value()) {
            if (pathMatcher.match(pattern, pathToCheck)) {
                return true;
            }
        }
        return false;
    }
}