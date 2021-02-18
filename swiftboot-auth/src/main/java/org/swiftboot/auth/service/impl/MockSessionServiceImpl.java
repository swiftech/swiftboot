package org.swiftboot.auth.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.swiftboot.auth.service.Session;
import org.swiftboot.auth.service.SessionService;

/**
 * Mock session management for development.
 * No matter what session token provided from caller client, the same session will be used,
 * unless addSession() called with a new mock session.
 * Restart application will invalid the mock sessions.
 * TODO
 *
 * @author swiftech
 */
public class MockSessionServiceImpl implements SessionService {

    private Logger log = LoggerFactory.getLogger(MockSessionServiceImpl.class);

    private Session mockSession;

    public MockSessionServiceImpl() {
        log.info("Use mock session service");
    }

    @Override
    public void addSession(String token, Session session) {
        log.warn("Mock addSession()");
        mockSession = session;
    }

    @Override
    public Session getSession(String token) {
        log.warn("Mock getSession()");
        return mockSession;
    }

    @Override
    public Session getSession(String group, String token) {
        log.warn("Mock getSession()");
        return mockSession;
    }

    @Override
    public void removeSession(String token) {
        log.warn("Mock removeSession()");
        mockSession = null;
    }

    @Override
    public void removeSession(String group, String token) {
        log.warn("Mock removeSession()");
        mockSession = null;
    }

    @Override
    public Session verifySession(String token) {
        log.warn("Mock verifySession()");
        if (mockSession != null
                && mockSession.getExpireTime() != null
                && mockSession.getExpireTime() > System.currentTimeMillis()) {
            return mockSession;
        }
        return null;
    }

    @Override
    public Session verifySession(String group, String token) {
        log.warn("Mock verifySession()");
        if (mockSession != null
                && mockSession.getExpireTime() != null
                && mockSession.getExpireTime() > System.currentTimeMillis()) {
            return mockSession;
        }
        return null;
    }
}
