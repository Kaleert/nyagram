package pro.kaleert.nyagram.api.meta;

import pro.kaleert.nyagram.api.objects.InputFile;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Map;

/**
 * Интерфейс для методов API, которые содержат файлы и должны отправляться как `multipart/form-data`.
 * <p>
 * Примерами таких методов являются {@link pro.kaleert.nyagram.api.methods.send.SendPhoto},
 * {@link pro.kaleert.nyagram.api.methods.send.SendDocument} и т.д.
 * Клиент использует этот интерфейс для извлечения файлов и формирования корректного HTTP-запроса.
 * </p>
 *
 * @since 1.0.0
 */
public interface MultipartRequest {
    
    /**
     * Возвращает карту файлов, которые необходимо отправить.
     *
     * @return Карта, где ключ — имя поля (например, "photo"), а значение — объект {@link InputFile}.
     */
    @JsonIgnore
    Map<String, InputFile> getFiles();
}