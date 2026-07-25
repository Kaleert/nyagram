package pro.kaleert.nyagram.pipeline;

import pro.kaleert.nyagram.command.CommandContext;
import pro.kaleert.nyagram.core.CommandResult;
import pro.kaleert.nyagram.meta.CommandMeta;
import pro.kaleert.nyagram.security.AccessDeniedAction;
import pro.kaleert.nyagram.security.spi.UserLevelProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import pro.kaleert.nyagram.api.objects.User;

import java.util.Optional;

/**
 * Пре-процессор для проверки уровня доступа пользователя.
 * <p>
 * Обрабатывает аннотацию {@link pro.kaleert.nyagram.security.LevelRequired}.
 * Сравнивает уровень пользователя (полученный от {@link pro.kaleert.nyagram.security.spi.UserLevelProvider})
 * с требуемым уровнем команды.
 * </p>
 *
 * @since 1.0.0
 */
@Slf4j
@Component
@Order(2)
@RequiredArgsConstructor
public class LevelCheckPreProcessor implements CommandPreProcessor {

    private final UserLevelProvider levelProvider;

    @Override
    public Optional<CommandResult> process(CommandContext context, CommandMeta commandMeta) {
        CommandMeta.LevelRequirement req = commandMeta.getLevelRequirement();
        if (req == null) {
            return Optional.empty();
        }

        User telegramUser = context.getTelegramUser();
        Integer userLevel = levelProvider.getUserLevel(telegramUser);

        if (userLevel == null) userLevel = 0;

        if (userLevel < req.min() || userLevel > req.max()) {
            return handleAccessDenied(req.deniedAction(), userLevel, req.min());
        }

        return Optional.empty();
    }

    private Optional<CommandResult> handleAccessDenied(AccessDeniedAction action, int current, int required) {
        switch (action) {
            case SILENT:
                return Optional.of(CommandResult.noResponse());
            
            case NOTIFY:
                return Optional.of(CommandResult.error(
                    String.format("⛔ <b>Доступ запрещен.</b>\nВаш уровень: %d\nТребуется: %d", current, required)
                ));
            
            case LOG_WARNING:
                log.warn("Access denied (Level mismatch). Current: {}, Required: {}", current, required);
                return Optional.of(CommandResult.error("⛔ Недостаточно прав для выполнения команды."));
                
            default:
                return Optional.of(CommandResult.error("⛔ Доступ запрещен."));
        }
    }
}