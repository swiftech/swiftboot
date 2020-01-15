package org.swiftboot.auth.service;

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

    public SessionBuilder setGroup(String group) {
        this.group = group;
        return this;
    }

    public SessionBuilder setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public SessionBuilder setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public SessionBuilder setExpireTime(Long expireTime) {
        this.expireTime = expireTime;
        return this;
    }

    public Session createSession() {
        return new Session(group, userId, userName, expireTime);
    }
}