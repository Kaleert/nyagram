package com.kaleert.nyagram.fsm.wizard;

import java.lang.annotation.*;

/**
 * Помечает метод как шаг внутри сценария.
 *
 * @since 1.2.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Step {
    int value();
}