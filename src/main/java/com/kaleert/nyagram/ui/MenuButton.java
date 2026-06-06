package com.kaleert.nyagram.ui;

import java.lang.annotation.*;

/**
 * Помечает метод как обработчик нажатия кнопки в декларативном меню.
 *
 * @since 1.2.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MenuButton {
    int row();
    String text();
    String style() default "";
}