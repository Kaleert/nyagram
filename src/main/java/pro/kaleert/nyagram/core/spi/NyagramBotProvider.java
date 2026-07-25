package pro.kaleert.nyagram.core.spi;

import java.util.Collection;

/**
 * Провайдер токенов для работы фреймворка в режиме Multi-Bot (SaaS).
 * <p>
 * Если вы реализуете этот интерфейс и зарегистрируете его как Spring Bean,
 * Nyagram автоматически перейдет в многопользовательский режим и установит
 * вебхуки для всех переданных токенов.
 * </p>
 *
 * @since 1.2.1
 */
public interface NyagramBotProvider {
    
    /**
     * Возвращает коллекцию токенов всех дочерних ботов.
     * @return Коллекция токенов.
     */
    Collection<String> getBotTokens();
}