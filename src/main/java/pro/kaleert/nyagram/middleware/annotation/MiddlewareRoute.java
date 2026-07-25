package pro.kaleert.nyagram.middleware.annotation;

import org.springframework.stereotype.Component;
import java.lang.annotation.*;

/**
 * Указывает паттерны путей команд, для которых должен срабатывать Middleware.
 * Поддерживает синтаксис Ant-путей (например, "/admin/**").
 *
 * @since 1.2.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface MiddlewareRoute {
    String[] value();
}