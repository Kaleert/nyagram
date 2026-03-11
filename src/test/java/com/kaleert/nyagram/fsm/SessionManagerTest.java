package com.kaleert.nyagram.fsm;

import com.kaleert.nyagram.fsm.spi.SessionStore;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SessionManagerTest {

    @Test
    void startSessionPersistsFurtherPutDataMutations() {
        CloningSessionStore store = new CloningSessionStore();
        SessionManager sessionManager = new SessionManager(store);

        UserSession session = sessionManager.startSession(1L, 2L, "FIRST");
        session.putData("tag", "#creator");

        UserSession reloaded = sessionManager.getSession(1L);
        assertNotNull(reloaded);
        assertEquals("#creator", reloaded.getData("tag", String.class));
        assertEquals("FIRST", reloaded.getState());
    }

    @Test
    void getSessionPersistsStateAndDataMutations() {
        CloningSessionStore store = new CloningSessionStore();
        SessionManager sessionManager = new SessionManager(store);
        sessionManager.saveSession(new UserSession(7L, 8L, "A", new ConcurrentHashMap<>(), null, null));

        UserSession session = sessionManager.getSession(7L);
        assertNotNull(session);

        session.putData("lang", "ru");
        session.setState("B");

        UserSession reloaded = sessionManager.getSession(7L);
        assertNotNull(reloaded);
        assertEquals("ru", reloaded.getData("lang", String.class));
        assertEquals("B", reloaded.getState());
    }

    private static final class CloningSessionStore implements SessionStore {

        private final Map<Long, UserSession> storage = new ConcurrentHashMap<>();

        @Override
        public void save(UserSession session) {
            storage.put(session.getUserId(), cloneSession(session));
        }

        @Override
        public Optional<UserSession> get(Long userId) {
            UserSession session = storage.get(userId);
            return Optional.ofNullable(session == null ? null : cloneSession(session));
        }

        @Override
        public void delete(Long userId) {
            storage.remove(userId);
        }

        @Override
        public void cleanupExpired(int timeoutMinutes) {
        }

        private UserSession cloneSession(UserSession session) {
            return new UserSession(
                    session.getUserId(),
                    session.getChatId(),
                    session.getState(),
                    new ConcurrentHashMap<>(session.getData() != null ? session.getData() : Map.of()),
                    session.getCreatedAt(),
                    session.getLastUpdatedAt()
            );
        }
    }
}
