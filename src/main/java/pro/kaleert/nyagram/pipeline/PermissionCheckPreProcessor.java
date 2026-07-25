package pro.kaleert.nyagram.pipeline;

import pro.kaleert.nyagram.command.CommandContext;
import pro.kaleert.nyagram.core.CommandResult;
import pro.kaleert.nyagram.exceptions.NoPermissionException;
import pro.kaleert.nyagram.meta.CommandMeta;
import pro.kaleert.nyagram.security.spi.UserPermissionProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;

/**
 * Пре-процессор для проверки прав (Permissions) пользователя.
 * <p>
 * Обрабатывает аннотацию {@link pro.kaleert.nyagram.security.RequiresPermission}.
 * Проверяет наличие у пользователя необходимых прав через {@link pro.kaleert.nyagram.security.spi.UserPermissionProvider}.
 * </p>
 *
 * @since 1.0.0
 */
@Slf4j
@Component
@Order(1)
@RequiredArgsConstructor
public class PermissionCheckPreProcessor implements CommandPreProcessor {

    private final Optional<UserPermissionProvider> permissionProvider;

    @Override
    public Optional<CommandResult> process(CommandContext context, CommandMeta commandMeta) {
        Set<String> requiredPermissions = commandMeta.getRequiredPermissions();

        if (requiredPermissions == null || requiredPermissions.isEmpty()) {
            return Optional.empty();
        }

        if (permissionProvider.isEmpty()) {
            log.warn("Command {} requires permissions {}, but no UserPermissionProvider bean found!", 
                    commandMeta.getFullCommandPath(), requiredPermissions);
            return Optional.of(CommandResult.error("❌ Ошибка конфигурации безопасности бота."));
        }

        UserPermissionProvider provider = permissionProvider.get();
        if (provider.isSuperAdmin(context.getTelegramUser())) {
            return Optional.empty();
        }

        Set<String> userPermissions = provider.getUserPermissions(context.getTelegramUser());

        for (String required : requiredPermissions) {
            if (!userPermissions.contains(required) && !userPermissions.contains("*")) {
                throw new NoPermissionException(required);
            }
        }

        return Optional.empty();
    }
}