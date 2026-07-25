package pro.kaleert.nyagram.fsm.wizard;

import org.springframework.stereotype.Component;
import java.lang.annotation.*;

/**
 * Помечает класс как сценарий (сцену) для пошагового выполнения.
 *
 * @since 1.2.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Scene {
    String value();
}