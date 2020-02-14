package org.swiftboot.shiro.config;

/**
 * @author swiftech
 **/
public class ShiroSessionConfigBean {

    /**
     * 会话超时时间,单位秒,默认30分钟
     */
    private int timeout = 30 * 60;

    /**
     * 强制禁用重定向
     */
    private boolean forceDisableRedirect = false;

    /**
     * Redis 分组名称,默认为 shiro-session
     */
    private String redisGroup = "shiro-session";

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

    public boolean isForceDisableRedirect() {
        return forceDisableRedirect;
    }

    public void setForceDisableRedirect(boolean forceDisableRedirect) {
        this.forceDisableRedirect = forceDisableRedirect;
    }
}
