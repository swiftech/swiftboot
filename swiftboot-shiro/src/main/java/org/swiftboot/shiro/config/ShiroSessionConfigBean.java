package org.swiftboot.shiro.config;

import org.swiftboot.shiro.constant.ShiroSessionStorageType;

/**
 * @author swiftech
 **/
public class ShiroSessionConfigBean {

    /**
     * 会话存储方式
     */
    private ShiroSessionStorageType storageType = ShiroSessionStorageType.memory;

    /**
     * 会话超时时间,单位秒,默认30分钟
     */
    private int timeout = 30 * 60;

    /**
     * Redis 分组名称,默认为 swiftboot-shiro-session
     */
    private String redisGroup = "swiftboot-shiro-session";

    public ShiroSessionStorageType getStorageType() {
        return storageType;
    }

    public void setStorageType(ShiroSessionStorageType storageType) {
        this.storageType = storageType;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public String getRedisGroup() {
        return redisGroup;
    }

    public void setRedisGroup(String redisGroup) {
        this.redisGroup = redisGroup;
    }

}
