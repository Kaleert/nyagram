package pro.kaleert.nyagram.dispatcher;

import pro.kaleert.nyagram.core.CommandResult;
import pro.kaleert.nyagram.api.objects.Update;
import java.util.concurrent.CompletableFuture;

/**
 * Диспетчер команд. Отвечает за распознавание команд в тексте сообщений
 * и вызов соответствующих обработчиков.
 *
 * @since 1.0.0
 */
public interface CommandDispatcher {
    
    /**
     * Пытается обработать обновление как команду.
     *
     * @param update Входящее обновление.
     * @return Future с результатом выполнения команды.
     */
    CompletableFuture<CommandResult> dispatch(Update update);
}