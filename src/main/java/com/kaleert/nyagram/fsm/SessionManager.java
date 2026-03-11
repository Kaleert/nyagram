package com.kaleert.nyagram.fsm;

import com.kaleert.nyagram.fsm.spi.SessionStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * Сервис управления пользовательскими сессиями (FSM).
 * <p>
 * Предоставляет высокоуровневый API для:
 * <ul>
 *     <li>Начала новой сессии (перехода в начальное состояние).</li>
 *     <li>Получения текущей сессии пользователя.</li>
 *     <li>Обновления состояния (перехода на следующий шаг).</li>
 *     <li>Очистки сессии (завершения диалога).</li>
 * </ul>
 * Делегирует хранение данных реализации {@link SessionStore}.
 * </p>
 *
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SessionManager {

    private final SessionStore sessionStore;

    @Value("${nyagram.fsm.ttl-minutes:30}")
    private int sessionTtlMinutes;
    
    /**
     * Получает активную сессию пользователя.
     *
     * @param userId ID пользователя.
     * @return Объект сессии или {@code null}, если сессия не найдена.
     */
    public UserSession getSession(Long userId) {
        return sessionStore.get(userId)
                .map(this::attachPersistence)
                .orElse(null);
    }
    
    /**
     * Начинает новую сессию диалога.
     * <p>
     * Создает новую запись в хранилище, перезаписывая старую, если она была.
     * </p>
     *
     * @param userId ID пользователя.
     * @param chatId ID чата, где началась сессия.
     * @param initialState Начальное состояние (State).
     * @return Созданный объект сессии.
     */
    public UserSession startSession(Long userId, Long chatId, String initialState) {
        UserSession session = new UserSession(userId, chatId);
        session.setState(initialState);
        saveSession(session);
        log.debug("Started FSM session for user {} in state {}", userId, initialState);
        return attachPersistence(session);
    }
    
    /**
     * Переводит пользователя в новое состояние.
     * <p>
     * Обновляет поле {@code state} в текущей сессии и сбрасывает таймер неактивности.
     * Если сессии нет, ничего не делает.
     * </p>
     *
     * @param userId ID пользователя.
     * @param newState Новое состояние.
     */
    public void updateState(Long userId, String newState) {
        UserSession session = getSession(userId);
        if (session != null) {
            session.setState(newState);
        }
    }

    public void saveSession(UserSession session) {
        if (session == null) {
            return;
        }

        sessionStore.save(toStoredSession(session));
    }
    
    /**
     * Принудительно завершает сессию пользователя.
     * <p>
     * Удаляет все данные сессии из хранилища. Пользователь переходит в состояние "без сессии".
     * </p>
     *
     * @param userId ID пользователя.
     */
    public void clearSession(Long userId) {
        sessionStore.delete(userId);
        log.debug("Cleared FSM session for user {}", userId);
    }
    
    /**
     * Периодическая задача очистки устаревших сессий.
     * <p>
     * Запускается по расписанию (настраивается через {@code nyagram.fsm.cleanup-interval}).
     * Удаляет сессии, которые не обновлялись дольше времени жизни (TTL).
     * </p>
     */
    @Scheduled(fixedRateString = "${nyagram.fsm.cleanup-interval:60000}")
    public void cleanup() {
        sessionStore.cleanupExpired(sessionTtlMinutes);
    }

    private UserSession attachPersistence(UserSession session) {
        if (session instanceof ManagedUserSession managed) {
            return managed;
        }

        return new ManagedUserSession(session, this::saveSession);
    }

    private UserSession toStoredSession(UserSession session) {
        Map<String, Object> copiedData = new ConcurrentHashMap<>();
        if (session.getData() != null) {
            copiedData.putAll(session.getData());
        }

        return new UserSession(
                session.getUserId(),
                session.getChatId(),
                session.getState(),
                copiedData,
                session.getCreatedAt(),
                session.getLastUpdatedAt()
        );
    }

    private static final class ManagedUserSession extends UserSession {

        private final transient Consumer<UserSession> persister;

        private ManagedUserSession(UserSession delegate, Consumer<UserSession> persister) {
            super(
                    delegate.getUserId(),
                    delegate.getChatId(),
                    delegate.getState(),
                    new ConcurrentHashMap<>(delegate.getData() != null ? delegate.getData() : Map.of()),
                    delegate.getCreatedAt(),
                    delegate.getLastUpdatedAt()
            );
            this.persister = persister;
        }

        @Override
        public void setState(String state) {
            super.setState(state);
            super.setLastUpdatedAt(LocalDateTime.now());
            persister.accept(this);
        }

        @Override
        public void setData(Map<String, Object> data) {
            super.setData(data != null ? new ConcurrentHashMap<>(data) : new ConcurrentHashMap<>());
            super.setLastUpdatedAt(LocalDateTime.now());
            persister.accept(this);
        }

        @Override
        public void putData(String key, Object value) {
            super.putData(key, value);
            persister.accept(this);
        }
    }
}
