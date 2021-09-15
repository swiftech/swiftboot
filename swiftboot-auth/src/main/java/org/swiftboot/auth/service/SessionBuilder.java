package org.swiftboot.auth.service;

import java.util.HashMap;
import java.util.Map;

/**
 * 构造用户会话 {@link Session}
 *
 * @see Session
 */
public class SessionBuilder {
    private String group;
    private String userId;
    private String userName;
    private Long expireTime;
    private final Map<String, Object> additions = new HashMap<>();

    public SessionBuilder group(String group) {
        this.group = group;
        return this;
    }

    public SessionBuilder userId(String userId) {
        this.userId = userId;
        return this;
    }

    public SessionBuilder userName(String userName) {
        this.userName = userName;
        return this;
    }

    public SessionBuilder expireTime(Long expireTime) {
        this.expireTime = expireTime;
        return this;
    }

    public  SessionBuilder addition(String key, Object value) {
        this.additions.put(key, value);
        return this;
    }

    public Session createSession() {
        Session ret = new Session(group, userId, userName, expireTime);
        ret.setAdditions(this.additions);
        return ret;
    }
}