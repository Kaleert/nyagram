package pro.kaleert.nyagram.exception;

import org.springframework.boot.diagnostics.AbstractFailureAnalyzer;
import org.springframework.boot.diagnostics.FailureAnalysis;

/**
 * Универсальный перехватчик фатальных ошибок при запуске Spring Boot.
 *
 * @since 1.2.1
 */
public class NyagramFailureAnalyzer extends AbstractFailureAnalyzer<Throwable> {

    @Override
    protected FailureAnalysis analyze(Throwable rootFailure, Throwable cause) {
        String message = cause.getMessage() != null ? cause.getMessage() : "";
        String stackTrace = cause.toString();

        if (message.contains("pro.kaleert.nyagram") || stackTrace.contains("pro.kaleert.nyagram")) {
            return new FailureAnalysis(
                "Обнаружено использование старого префикса пакетов (pro.kaleert.nyagram) в конфигурации или рефлексии.",
                "Изучите гайд по миграции на версию 1.2.1: https://nyagram.kaleert.pro/docs/migration-to-1.2.1",
                cause
            );
        }

        if (message.contains("NyagramBotProvider") || message.contains("NyagramClient")) {
            return new FailureAnalysis(
                "Ошибка инициализации ядра Nyagram. Отсутствует необходимый Bean или неверная конфигурация.",
                "Убедитесь, что ваш config.yml настроен корректно. Документация: https://nyagram.kaleert.pro/docs/configuration",
                cause
            );
        }

        return null;
    }
}