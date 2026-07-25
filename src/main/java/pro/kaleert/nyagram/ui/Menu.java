package pro.kaleert.nyagram.ui;

import org.springframework.stereotype.Component;
import java.lang.annotation.*;

/**
 * Помечает класс как декларативное меню с Inline-кнопками.
 *
 * @since 1.2.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Menu {
    String id();
    String text() default "";
}