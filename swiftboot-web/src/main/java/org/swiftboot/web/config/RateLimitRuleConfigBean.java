package org.swiftboot.web.config;

import org.swiftboot.web.constant.LimitType;

/**
 * Configuration for a single rate limit rule.
 * Used to configure rate limiting for API endpoints via application.yaml or configuration classes.
 *
 * @since 3.1.4
 */
public class RateLimitRuleConfigBean {

    /**
     * URI pattern to match the endpoint, supports Ant-style path patterns.
     * For example: /health/**, /api/user/*, /admin/**
     */
    private String uri;

    /**
     * Limit time in milliseconds, default is 1000 milliseconds.
     */
    private long time = 1000;

    /**
     * Limit count, default is 10.
     */
    private int count = 10;

    /**
     * Global or user specific, default is global.
     */
    private LimitType limitType = LimitType.DEFAULT;

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public LimitType getLimitType() {
        return limitType;
    }

    public void setLimitType(LimitType limitType) {
        this.limitType = limitType;
    }
}
