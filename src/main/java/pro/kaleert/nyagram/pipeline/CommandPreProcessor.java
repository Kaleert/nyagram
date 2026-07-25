package pro.kaleert.nyagram.pipeline;

import pro.kaleert.nyagram.core.CommandResult;
import pro.kaleert.nyagram.command.CommandContext;
import pro.kaleert.nyagram.meta.CommandMeta;

import java.util.Optional;

/**
 * Интерфейс пре-процессора команд.
 * <p>
 * Выполняется непосредственно перед вызовом метода команды, но уже после Middleware.
 * Предназначен для проверок, специфичных для метаданных команды (права доступа, лимиты, уровни).
 * </p>
 *
 * @since 1.0.0
 */
public interface CommandPreProcessor {
    
    /**
     * Выполняет предварительную проверку.
     *
     * @param context Контекст команды.
     * @param commandMeta Метаданные команды.
     * @return {@code Optional.empty()} если проверку пройдена, или {@code CommandResult} с ошибкой, если нужно прервать выполнение.
     */
    Optional<CommandResult> process(CommandContext context, CommandMeta commandMeta);
}
