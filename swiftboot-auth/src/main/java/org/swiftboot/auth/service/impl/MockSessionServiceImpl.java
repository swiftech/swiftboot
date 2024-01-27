package org.swiftboot.auth.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.swiftboot.auth.service.Session;
import org.swiftboot.auth.service.SessionService;
import org.swiftboot.util.IoUtils;
import org.swiftboot.util.JsonUtils;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Mock session management for development.
 * No matter what session token provided from caller client, the same session will be used,
 * unless addSession() called with a new mock session.
 * the mock session is dumped to a file and restart application will restore session from it.
 *
 * @author swiftech
 */
public class MockSessionServiceImpl implements SessionService, ApplicationContextAware {

    private static final Logger log = LoggerFactory.getLogger(MockSessionServiceImpl.class);

    private ApplicationContext applicationContext;

    private Session mockSession;

    private String dumpFileName; // will be replaced with spring boot application id

    public MockSessionServiceImpl() {
        log.info("Use mock session service");
    }

    @PostConstruct
    public void init() {
        log.debug(applicationContext.getApplicationName());
        String appName = StringUtils.replaceChars(applicationContext.getId(), ' ', '_');
        if (StringUtils.isNotBlank(appName)) {
            dumpFileName = appName;
        }
        else {
            dumpFileName = "session_file";
        }
        mockSession = this.restoreFromFile();
    }

    @Override
    public void addSession(String token, Session session) {
        log.warn("Mock addSession()");
        mockSession = session;
        this.dumpToFile();
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
        this.dumpToFile();
    }

    @Override
    public void removeSession(String group, String token) {
        log.warn("Mock removeSession()");
        mockSession = null;
        this.dumpToFile();
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

    @Override
    public void clearAllSessions() {
        log.warn("Mock clearAllSessions()");
        mockSession = null;
        this.dumpToFile();
    }

    private void dumpToFile() {
        String s = null;
        try {
            s = JsonUtils.object2Json(mockSession);
            File dumpFile = new File(SystemUtils.getJavaIoTmpDir(), dumpFileName);
            log.debug("dump session to file: " + dumpFile);
            if (!dumpFile.getParentFile().exists()) {
                dumpFile.getParentFile().mkdirs();
            }
            FileOutputStream fos = new FileOutputStream(dumpFile);
            fos.write(s.getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Session restoreFromFile() {
        try {
            File dumpFile = new File(SystemUtils.getJavaIoTmpDir(), dumpFileName);
            if (!dumpFile.exists()) {
                return null;
            }
            log.debug("restore session from file: " + dumpFile);
            FileInputStream fis = new FileInputStream(dumpFile);
            byte[] bytes = IoUtils.readAllToBytes(fis);
            if (bytes == null) {
                return null;
            }
            return JsonUtils.jsonTo(new String(bytes), Session.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
