package org.swiftboot.auth.config;

import org.apache.commons.lang3.StringUtils;

/**
 * swiftboot.auth.session
 *
 * @author swiftech 2019-06-10
 **/
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
     * 会话令牌的名称，作为客户端保存和传递时的 key
     */
    private String tokenKey = "swiftboot_token";

    /**
     * 会话超时时间, 单位秒, 默认30分钟，设置为 0 或者 <0 表示不超时
     */
    private int expiresIn = 60 * 30;

    /**
     * 当用户访问的时候是否更新会话的超时时间（只在 expiresIn > 0 的时候有效），默认为 false
     */
    private boolean updateExpireTime = false;


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

    public String getTokenKey() {
        return tokenKey;
    }

    public void setTokenKey(String tokenKey) {
        this.tokenKey = tokenKey;
    }

    public boolean isUpdateExpireTime() {
        return updateExpireTime;
    }

    public void setUpdateExpireTime(boolean updateExpireTime) {
        this.updateExpireTime = updateExpireTime;
    }
}
