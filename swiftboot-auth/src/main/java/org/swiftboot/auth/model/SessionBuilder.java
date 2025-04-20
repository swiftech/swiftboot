package org.swiftboot.auth.model;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 构造用户会话 {@link Session}
 *
 * @author swiftech
 * @see Session
 */
public class SessionBuilder {

    /**
     * The user token here is used for {@link org.swiftboot.auth.interceptor.UserSessionResponseAdvice} to read.
     *
     */
    private String userToken;

    /**
     * 会话分组名称，如果不提供，会话会被分配到默认的分组
     */
    private String group;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 会话超时时间，单位毫秒，设为 null 表示不超时
     */
    private Long expireTime;

    private final Map<String, Object> additions = new HashMap<>();

    public SessionBuilder userToken(String userToken) {
        this.userToken = userToken;
        return this;
    }

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

    public SessionBuilder addition(String key, Object value) {
        if (StringUtils.isNotBlank(key) && value != null) {
            this.additions.put(key, value);
        }
        return this;
    }

    public Session createSession() {
        Session ret = new Session(userToken, group, userId, userName, expireTime);
        if (!this.additions.isEmpty()) {
            ret.setAdditions(this.additions);
        }
        return ret;
    }
}