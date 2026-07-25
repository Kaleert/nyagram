package pro.kaleert.nyagram.i18n;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.SafeConstructor;
import org.yaml.snakeyaml.LoaderOptions;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Сервис интернационализации (I18N).
 * <p>
 * Загружает переводы из YAML-файлов ({@code messages_ru.yml}, {@code messages_en.yml})
 * и предоставляет методы для получения локализованных строк по ключу.
 * Поддерживает иерархию (fallback) локалей: User Locale -> Language Locale -> Default Locale.
 * </p>
 *
 * @since 1.0.0
 */
@Service
public class LocaleService {

    private static final Logger log = LoggerFactory.getLogger(LocaleService.class);
    private final Map<Locale, Map<String, String>> translations = new ConcurrentHashMap<>();

    @Value("${nyagram.i18n.default-locale:en}")
    private String defaultLocaleCode;
    private Locale defaultLocale;
    
    private static final String NOT_FOUND_TEMPLATE = "[MISSING_TRANSLATION: %s]";
    
    /**
     * Получает сырую строку перевода по ключу.
     *
     * @param locale Локаль, для которой нужен перевод.
     * @param key Ключ сообщения (например, "menu.title").
     * @return Найденная строка или шаблон ошибки, если ключ не найден ни в одной локали.
     */
    @PostConstruct
    public void init() {
        this.defaultLocale = Locale.forLanguageTag(defaultLocaleCode);
        loadTranslations();
    }

    private void loadTranslations() {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
            Resource[] resources = resolver.getResources("classpath*:/**/messages_*.yml");
            log.info("Found {} localization files to load.", resources.length);

            for (Resource resource : resources) {
                String filename = resource.getFilename();
                String localeCode = Optional.ofNullable(filename)
                        .filter(f -> f.matches("messages_[a-z]{2}(_[A-Z]{2})?\\.yml"))
                        .map(f -> f.substring("messages_".length(), f.length() - ".yml".length()))
                        .orElse(null);

                if (localeCode == null) {
                    log.warn("Skipping malformed localization file name: {}", filename);
                    continue;
                }
                
                Locale locale = Locale.forLanguageTag(localeCode.replace('_', '-'));
                LoaderOptions loaderOptions = new LoaderOptions();
                SafeConstructor safeConstructor = new SafeConstructor(loaderOptions);
                Yaml yaml = new Yaml(safeConstructor);
                Object loaded = yaml.load(resource.getInputStream());

                if (!(loaded instanceof Map)) {
                    log.warn("Skipping {} — parsed content is not a mapping", filename);
                    continue;
                }

                Map<?, ?> rawData = (Map<?, ?>) loaded;

                Map<String, String> flatTranslations;
                try {
                    log.info("Parsing localization file: {}", filename);
                    flatTranslations = flattenMap(rawData);
                } catch (RuntimeException ex) {
                    log.error("Failed to flatten translations for file {}", filename, ex);
                    throw ex;
                }

                translations.put(locale, flatTranslations);

                log.info("Successfully loaded {} translations for locale {} from {}", 
                        flatTranslations.size(), locale, filename);
            }
        } catch (IOException e) {
            log.error("Failed to load localization files.", e);
        }
    }
    
    private Map<String, String> flattenMap(Map<?, ?> source) {
        Map<String, String> result = new ConcurrentHashMap<>();
        flatten(source, "", result);
        return result;
    }

    private void flatten(Map<?, ?> source, String prefix, Map<String, String> result) {
        source.forEach((k, v) -> {
            String keyStr = String.valueOf(k);
            String newKey = prefix.isEmpty() ? keyStr : prefix + "." + keyStr;
            if (v instanceof Map) {
                flatten((Map<?, ?>) v, newKey, result);
            } else if (v != null) {
                result.put(newKey, v.toString());
            }
        });
    }

    /**
     * Основной метод для получения перевода. Возвращает TranslationResolver для подстановки параметров.
     * @param locale Локаль пользователя.
     * @param key Ключ перевода (например, "profile", "error.general").
     * @return TranslationResolver для текучего построения сообщения.
     */
    public TranslationResolver translate(Locale locale, String key) {
        String translation = findTranslation(locale, key);
        return new TranslationResolver(translation);
    }
    
    /**
     * Получает сырую строку перевода по ключу.
     *
     * @param locale Локаль, для которой нужен перевод.
     * @param key Ключ сообщения (например, "menu.title").
     * @return Найденная строка или шаблон ошибки, если ключ не найден ни в одной локали.
     */
    public String getTranslation(Locale locale, String key) {
        return findTranslation(locale, key);
    }

    /**
     * Находит строку перевода с учетом резервных локалей.
     * @param locale Локаль для поиска.
     * @param key Ключ перевода.
     * @return Найденная строка или [MISSING_TRANSLATION: key].
     */
    private String findTranslation(Locale locale, String key) {
        String translation = Optional.ofNullable(translations.get(locale))
                .map(map -> map.get(key))
                .orElse(null);

        if (translation != null) {
            return translation;
        }

        Locale languageLocale = Locale.forLanguageTag(locale.getLanguage());
        if (!locale.equals(languageLocale)) {
            translation = Optional.ofNullable(translations.get(languageLocale))
                    .map(map -> map.get(key))
                    .orElse(null);
            
            if (translation != null) {
                log.debug("Using language fallback for key '{}' from {} to {}", key, locale, languageLocale);
                return translation;
            }
        }
        
        if (!locale.equals(defaultLocale)) {
             translation = Optional.ofNullable(translations.get(defaultLocale))
                    .map(map -> map.get(key))
                    .orElse(null);
            
            if (translation != null) {
                log.debug("Using default locale fallback for key '{}' from {} to {}", key, locale, defaultLocale);
                return translation;
            }
        }

        log.warn("Translation not found for key '{}' in locales: {}, {}", key, locale, defaultLocale);
        return String.format(NOT_FOUND_TEMPLATE, key);
    }
}