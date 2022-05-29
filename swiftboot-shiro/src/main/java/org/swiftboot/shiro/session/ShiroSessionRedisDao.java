package org.swiftboot.shiro.session;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.swiftboot.service.service.RedisService;
import org.swiftboot.shiro.config.SwiftbootShiroConfigBean;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import java.io.*;
import java.util.Collection;

/**
 * 用 Redis 来实现会话的存储（或者说缓存）
 *
 * @author swiftech
 * @since 1.2
 **/
public class ShiroSessionRedisDao extends AbstractSessionDAO {

    private final Logger log = LoggerFactory.getLogger(ShiroSessionRedisDao.class);

    @Resource
    private RedisService redisService;

    @Resource
    private SwiftbootShiroConfigBean config;

    public ShiroSessionRedisDao() {
        log.info("Store session in Redis");
    }

    @Override
    protected Serializable doCreate(Session session) {
        // 通过 Shiro 的机制生成会话 ID
        Serializable sessionId = generateSessionId(session);
        assignSessionId(session, sessionId);

        if (log.isInfoEnabled()) {
            log.info(String.format("[%s] Create session for token %s", Thread.currentThread().getId(), sessionId));
        }
        // 将会话存入 Redis, 以会话 ID 作为 key 保存
        this.saveSession(String.valueOf(sessionId), session);
        if (log.isInfoEnabled()) {
            log.info("  - " + session);
        }
        return sessionId;
    }

    @Override
    protected Session doReadSession(Serializable sessionId) {
        if (log.isTraceEnabled()) {
            log.trace(String.format("[%s] Read session：%s", Thread.currentThread().getId(), sessionId));
        }
        String token = sessionId.toString();
        try {
            return this.loadSession(token);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void update(Session session) throws UnknownSessionException {
        if (log.isTraceEnabled()) {
            log.trace(String.format("[%s] Update session for %s", Thread.currentThread().getId(), session.getId()));
        }
        for (Object attributeKey : session.getAttributeKeys()) {
            if (log.isTraceEnabled()) {
                log.trace(String.format("  Session attr: %s - %s", attributeKey, session.getAttribute(attributeKey)));
            }
        }
        this.saveSession((String) session.getId(), session);
    }

    @Override
    public void delete(Session session) {
        String token = (String) session.getId();
        if (StringUtils.isBlank(token)) {
            log.warn(String.format("Delete session failed, token=: %s", token));
            return;
        }
        Session loadSession = loadSession(token);
        if (loadSession == null) {
            log.warn("Session not exist to delete");
            return;
        }
        if (log.isInfoEnabled()) {
            log.info(String.format("[%s] Delete session：%s", Thread.currentThread().getId(), session.getId()));
        }
        Jedis jedis = redisService.getJedis();
        jedis.hdel(config.getSession().getRedisGroup().getBytes(), token.getBytes());
        jedis.close();
    }

    @Override
    public Collection<Session> getActiveSessions() {
        return null;
    }


    /**
     * 将会话以 sessionId 为 key 保存在 {@link org.swiftboot.shiro.config.ShiroSessionConfigBean} 配置的 redis 分组中
     *
     * @param sessionId
     * @param session
     */
    private void saveSession(String sessionId, Session session) {
        if (StringUtils.isBlank(sessionId) || session == null) {
            throw new IllegalArgumentException("用户会话错误");
        }
//        log.debug(String.format("Session, host: %s, timeout: %s", session.getHost(), session.getTimeout()));

        if (log.isTraceEnabled()) {
            if (session.getTimeout() <= 0) {
                log.trace(String.format("  Store session %s (no expiration)", sessionId));
            }
            else {
                log.trace(String.format("  Store session %s, which expired after %d (%s) minutes", sessionId,
                        ShiroSessionUtils.expiresInMinutes(session), ShiroSessionUtils.expiresAt(session)));
            }
        }

        byte[] bSession = null;
        try {
            bSession = dumpSession(session);
            if (bSession == null || bSession.length == 0) {
                throw new RuntimeException("Serialize session fail: " + session.getClass().getName());
            }
            Jedis jedis = redisService.getJedis();
            jedis.hset(config.getSession().getRedisGroup().getBytes(), sessionId.getBytes(), bSession);
            jedis.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Store session fail");
        }
    }

    private Session loadSession(String token) {
        Jedis jedis = redisService.getJedis();
        byte[] bSession = jedis.hget(config.getSession().getRedisGroup().getBytes(), token.getBytes());
        jedis.close();

        if (bSession == null || bSession.length == 0) {
            if (log.isWarnEnabled()) {
                log.warn(String.format("Load session %s fail", token));
            }
            return null;
        }
        try {
            Session session = parseSession(bSession);
            if (log.isTraceEnabled()) {
                ShiroSessionUtils.printSession(session);
            }
            return session;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private byte[] dumpSession(Session session) throws Exception {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(os);
        objectOutputStream.writeObject(session);
        return os.toByteArray();
    }

    private Session parseSession(byte[] bSession) throws Exception {
        ByteArrayInputStream in = new ByteArrayInputStream(bSession);
        ObjectInputStream objectInputStream = new ObjectInputStream(in);
        return (Session) objectInputStream.readObject();
    }
}
