package pro.kaleert.nyagram.fsm.wizard;

import pro.kaleert.nyagram.command.CommandContext;
import pro.kaleert.nyagram.fsm.SessionManager;
import pro.kaleert.nyagram.fsm.UserSession;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Базовый класс для всех сценариев.
 * Предоставляет методы для управления шагами.
 *
 * @since 1.2.0
 */
public abstract class Wizard {

    @Autowired
    protected SessionManager sessionManager;

    /**
     * Переход к следующему шагу сценария.
     *
     * @param ctx Контекст команды.
     * @param nextStep Номер следующего шага.
     */
    protected void nextStep(CommandContext ctx, int nextStep) {
        UserSession session = sessionManager.getSession(ctx.getUserId());
        if (session != null) {
            String sceneName = this.getClass().getAnnotation(Scene.class).value();
            session.setState(sceneName + ":" + nextStep);
        }
    }

    /**
     * Завершает текущий сценарий и очищает сессию.
     *
     * @param ctx Контекст команды.
     */
    protected void finish(CommandContext ctx) {
        sessionManager.clearSession(ctx.getUserId());
    }
}