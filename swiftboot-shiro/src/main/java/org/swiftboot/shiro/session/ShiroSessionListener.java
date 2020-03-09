package org.swiftboot.shiro.session;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author swiftech
 * @since 1.2
 **/
public class ShiroSessionListener implements SessionListener {
    
    Logger log = LoggerFactory.getLogger(ShiroSessionListener.class);
    
    @Override
    public void onStart(Session session) {
        log.info("Session started:");
        ShiroSessionUtils.printSession(session);
    }

    @Override
    public void onStop(Session session) {
        log.info("Session stopped:");
        ShiroSessionUtils.printSession(session);
    }

    @Override
    public void onExpiration(Session session) {
        log.info("Session expired:");
        ShiroSessionUtils.printSession(session);
    }
}
