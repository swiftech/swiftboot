package org.swiftboot.auth.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * swiftboot.auth.session
 *
 * @author swiftech 2019-06-10
 * @see AuthConfigBean
 **/
@Configuration
@ConfigurationProperties("swiftboot.auth.session")
public class SessionConfigBean {

    /**
     * 会话管理类型，支持的选项有："redis"，"mock"
     * 默认使用 "redis"，设置后用 Redis 来管理用户会话；
     * 选项 "mock" 仅用于调试，会在内存中产生唯一的会话。
     */
    private String type = "redis";

    /**
     * 用来隔离不同类型的会话，不同的系统设置不同的分组，就可以在同一个 Redis 中管理不同系统的会话。
     * 默认不设置就不做分组处理
     *
     */
    private String group = StringUtils.EMPTY;

    /**
     * 会话超时时间长度, 单位秒, 默认30分钟，设置为 0 或者 <0 表示不超时
     */
    private int expiresIn = 60 * 30;

    /**
     * 当用户访问的时候是否更新会话的超时时间（只在 expiresIn > 0 的时候有效），默认为 false
     */
    private boolean updateExpireTime = false;

    /**
     * 启用 Cookie 来传递用户 Token，默认为不启用。
     */
    private boolean useCookie = false;

    /**
     * 设置 Cookie 中用户 Token 的有效路径，默认为 "/"。
     */
    private String cookiePath = "/";


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }

    public boolean isUpdateExpireTime() {
        return updateExpireTime;
    }

    public void setUpdateExpireTime(boolean updateExpireTime) {
        this.updateExpireTime = updateExpireTime;
    }

    public boolean isUseCookie() {
        return useCookie;
    }

    public void setUseCookie(boolean useCookie) {
        this.useCookie = useCookie;
    }

    public String getCookiePath() {
        return cookiePath;
    }

    public void setCookiePath(String cookiePath) {
        this.cookiePath = cookiePath;
    }
}
