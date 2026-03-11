package com.kaleert.nyagram.i18n;

import java.util.Locale;

/**
 * Интерфейс для определения локали пользователя "на лету".
 * @since 1.1.4
 */
public interface LocaleResolver {
    Locale resolve(Long userId);
}