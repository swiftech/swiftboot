package org.swiftboot.auth.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.swiftboot.common.auth.token.Authenticated;

import java.util.HashMap;
import java.util.Map;

/**
 * 表示用户会话，包含分组，用户 ID，用户名和会话过期时间。
 * 还可以用 additions 添加用户自定义的参数
 *
 * @author swiftech
 * @see SessionBuilder
 * @see org.swiftboot.auth.interceptor.UserSessionResponseAdvice
 */
public class Session implements Authenticated {

    /**
     * The user token here is used for {@link org.swiftboot.auth.interceptor.UserSessionResponseAdvice} to read.
     */
    @JsonIgnore
    private String userToken;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 会话分组名称，如果不提供，会话会被分配到默认的分组
     */
    private String group;

    /**
     * 会话超时时间，单位毫秒，设为 null 表示不超时
     */
    private Long expireTime;

    /**
     * 存储在会话中的额外信息，不要存储太大的对象
     */
    private Map<String, Object> additions = new HashMap<>();

    public Session() {
    }

    protected Session(String userToken, String group, String userId, String userName, Long expireTime) {
        this.userToken = userToken;
        this.userId = userId;
        this.userName = userName;
        this.group = group;
        this.expireTime = expireTime;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Long expireTime) {
        this.expireTime = expireTime;
    }

    public Map<String, Object> getAdditions() {
        return additions;
    }

    public void setAdditions(Map<String, Object> additions) {
        this.additions = additions;
    }
}
