package pro.kaleert.nyagram.api.objects.widget;

import java.nio.charset.StandardCharsets;
import java.util.zip.Deflater;
import pro.kaleert.nyagram.api.exception.TelegramApiValidationException;

/**
 * Проверки ограничений размеров данных виджетов Telegram Bot API.
 * @since 1.2.4
 */
public final class WidgetLimits {
    private WidgetLimits() {}

    /**
     * Проверяет длину строки в байтах UTF-8.
     *
     * @param value проверяемое значение
     * @param min минимальное количество байтов
     * @param max максимальное количество байтов
     * @param field имя проверяемого поля
     * @param path имя метода Telegram Bot API для сообщения об ошибке
     * @throws TelegramApiValidationException если длина вне допустимого диапазона
     */
    public static void utf8(String value, int min, int max, String field, String path) throws TelegramApiValidationException {
        if (value == null) {
            if (min > 0) throw new TelegramApiValidationException(field + " required", path);
            return;
        }
        int bytes = value.getBytes(StandardCharsets.UTF_8).length;
        if (bytes < min || bytes > max)
            throw new TelegramApiValidationException(field + " must be " + min + ".." + max + " UTF-8 bytes", path);
    }

    /**
     * Проверяет ограничения размера HTML-содержимого виджета.
     * Допускает от 1 до 32768 байтов UTF-8 и не более 4096 байтов
     * после сжатия zlib.
     *
     * @param value HTML-содержимое
     * @param path имя метода Telegram Bot API для сообщения об ошибке
     * @throws TelegramApiValidationException если превышены ограничения
     */
    public static void html(String value, String path) throws TelegramApiValidationException {
        utf8(value, 1, 32768, "html", path);
        byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
        Deflater zlib = new Deflater(9); // maximal zlib compression, not raw deflate
        try {
            zlib.setInput(bytes);
            zlib.finish();
            byte[] result = new byte[4097];
            int written = zlib.deflate(result);
            if (written > 4096 || !zlib.finished())
                throw new TelegramApiValidationException("html exceeds 4096 zlib-compressed bytes", path);
        } finally {
            zlib.end();
        }
    }
}
