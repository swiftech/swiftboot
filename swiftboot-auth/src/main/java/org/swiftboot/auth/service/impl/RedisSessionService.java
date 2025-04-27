package org.swiftboot.auth.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.swiftboot.auth.config.AuthConfigBean;
import org.swiftboot.auth.model.Session;
import org.swiftboot.auth.service.SessionService;
import org.swiftboot.web.exception.ErrMessageException;
import org.swiftboot.web.exception.ErrorCodeSupport;

import java.io.IOException;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * 用 Redis 实现会话（Session）控制，客户端采用 Jedis
 * 当部署集群时或多服务器时启用，非集群时使用 {@code SessionServiceImpl}
 *
 * @author swiftech
 */
public class RedisSessionService implements SessionService {

    private static final Logger log = LoggerFactory.getLogger(RedisSessionService.class);

    private final ObjectMapper mapper = new ObjectMapper();

    @Resource
    private StringRedisTemplate strRedisTemplate;

    @Resource
    private AuthConfigBean config;

    @Override
    public void addSession(String token, Session session) throws RuntimeException {
        if (isBlank(token) || session == null) {
            throw new RuntimeException("Params for session not provided");
        }
        try {
            // Session 中的超时时间覆盖配置中的超时时间
            if (session.getExpireTime() == null) {
                if (config.getSession().getExpiresIn() > 0) {
                    session.setExpireTime(System.currentTimeMillis() + (config.getSession().getExpiresIn() * 1000L));
                }
            }
            else {
                if (session.getExpireTime() <= 0) {
                    // session 没有超时时间
                    session.setExpireTime(null);
                }
            }
            this.saveSession(token, session);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException(String.format("Save session of %s failed", session.getUserId()));
        }
    }

    private void saveSession(String token, Session session) throws JsonProcessingException {
        byte[] bytes = mapper.writeValueAsBytes(session);
        if (isBlank(session.getGroup())) {
            if (StringUtils.isNotBlank(config.getSession().getGroup())) {
                strRedisTemplate.opsForHash().put(config.getSession().getGroup(), token, new String(bytes));
//                    ret = redisTemplate.hset(config.getSession().getGroup(), token, new String(bytes));// group -> token ->  会话
                log.debug(String.format("Session %s saved, expired at %s", token,
                        session.getExpireTime() == null ? "never" : DateFormatUtils.format(session.getExpireTime(), "yyyy-MM-dd HH:mm:ss")));
            }
            else {
                strRedisTemplate.opsForValue().set(token, new String(bytes)); // token ->  会话
//                    jedis.set(token.getBytes(), bytes); // token ->  会话
            }
        }
        else {
            strRedisTemplate.opsForHash().put(session.getGroup(), token, new String(bytes));// group -> token ->  会话
//            ret = jedis.hset(session.getGroup(), token, new String(bytes));// group -> token ->  会话
            log.debug(String.format("Session %s saved, expired at %s", token,
                    session.getExpireTime() == null ? "never" : DateFormatUtils.format(session.getExpireTime(), "yyyy-MM-dd HH:mm:ss")));
        }
    }

    @Override
    public Session getSession(String token) {
        return getSession(config.getSession().getGroup(), token);
    }

    @Override
    public Session getSession(String group, String token) {
        if (isBlank(token)) {
            return null;
        }

        if (isBlank(group)) {
            group = config.getSession().getGroup();
        }
        log.debug(String.format("token: %s", token));
        byte[] bytes = null;
        if (StringUtils.isNotBlank(group)) {
            Object o = strRedisTemplate.opsForHash().get(group, token);
            if (o != null) {
                if (isBlank(o.toString())) {
                    return null;
                }
                else {
                    bytes = o.toString().getBytes();
                }
            }
        }
        else {
            Object value = strRedisTemplate.opsForValue().get(token);
            if (value == null) {
                return null;
            }
            bytes = value.toString().getBytes();
            if (bytes.length == 0) {
                return null;
            }
        }
        if (bytes == null) {
            return null;
        }
        log.debug(new String(bytes));
        try {
            return mapper.readValue(bytes, Session.class);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Session is invalid");
        }
    }

    @Override
    public void removeSession(String token) {
        this.removeSession(config.getSession().getGroup(), token);
    }

    @Override
    public void removeSession(String group, String token) {
        Long result;
        if (StringUtils.isNotBlank(group)) {
            result = strRedisTemplate.opsForHash().delete(group, token);
//            result = redisTemplate.hdel(group, token);
        }
        else {
            if (StringUtils.isNotBlank(config.getSession().getGroup())) {
                result = strRedisTemplate.opsForHash().delete(config.getSession().getGroup(), token);
//                result = jedis.hdel(config.getSession().getGroup(), token);
            }
            else {
                strRedisTemplate.opsForValue().getAndDelete(token);
                result = 1L; // workaround
            }
        }

        if (result <= 0) {
            throw new RuntimeException("Remove session failed, token: %s".formatted(token));
        }
    }

    @Override
    public Session verifySession(String token) {
        return this.verifySession(config.getSession().getGroup(), token);
    }

    @Override
    public Session verifySession(String group, String token) {
        Session session = null;
        try {
            session = this.getSession(group, token);
        } catch (Exception e) {
            e.printStackTrace();
            log.warn(String.format("Retrieve session failed: %s", token));
            throw new ErrMessageException(ErrorCodeSupport.CODE_SYS_ERR);
        }

        if (session == null) {
            throw new ErrMessageException(ErrorCodeSupport.CODE_USER_SESSION_NOT_EXIST);
        }

        if (session.getExpireTime() != null) {
            if (session.getExpireTime() < System.currentTimeMillis()) {
                // Delete session if it is already timeout
                this.removeSession(group, token);
                throw new ErrMessageException(ErrorCodeSupport.CODE_SESSION_TIMEOUT);
            }
            else {
                if (config.getSession().isUpdateExpireTime()) {
                    // Update expire time if allowed and there is expired time in session
                    session.setExpireTime(System.currentTimeMillis() + (config.getSession().getExpiresIn() * 1000L));
                    try {
                        this.saveSession(token, session);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                        throw new RuntimeException(String.format("Save session of %s failed", session.getUserId()));
                    }
                }
                return session;
            }
        }
        return session;
    }

    @Override
    public void clearAllSessions() {
        if (StringUtils.isNoneBlank(config.getSession().getGroup())) {
            strRedisTemplate.delete(config.getSession().getGroup());
        }
    }
}
