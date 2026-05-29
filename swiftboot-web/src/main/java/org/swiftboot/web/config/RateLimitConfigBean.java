package org.swiftboot.web.config;

import java.util.ArrayList;
import java.util.List;

/**
 * Rate limit configuration that can be defined in application.yaml or configuration classes.
 * Provides an alternative to the {@link org.swiftboot.web.annotation.RateLimit} annotation approach.
 *
 * <p>Example configuration in application.yaml:</p>
 * <pre>
 * swiftboot:
 *   web:
 *     rate-limit:
 *       rules:
 *         - uri: /health/**
 *           time: 1000
 *           count: 10
 *           limit-type: DEFAULT
 *         - uri: /api/user/**
 *           time: 5000
 *           count: 5
 *           limit-type: USER
 * </pre>
 *
 * @since 3.1.4
 * @see RateLimitRuleConfigBean
 */
public class RateLimitConfigBean {

    /**
     * List of rate limit rules. Each rule defines a URI pattern and its corresponding limit settings.
     */
    private List<RateLimitRuleConfigBean> rules = new ArrayList<>();

    public List<RateLimitRuleConfigBean> getRules() {
        return rules;
    }

    public void setRules(List<RateLimitRuleConfigBean> rules) {
        this.rules = rules;
    }
}
