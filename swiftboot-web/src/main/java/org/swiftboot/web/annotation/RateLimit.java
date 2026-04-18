package org.swiftboot.web.annotation;

import org.swiftboot.web.aop.RateLimitAspect;
import org.swiftboot.web.constant.LimitType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to limit the request frequency of an API. The limit can be applied globally to the API or to each user.
 *
 * @since 3.1.1
 * @see RateLimitAspect
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimit {

    /**
     * Limit time in milliseconds, default is 1000 milliseconds.
     * @return
     */
    long time() default 1000;

    /**
     * Limit count, default is 10
     *
     * @return
     */
    int count() default 10;

    /**
     * Global or user specific, default is global.
     *
     * @return
     */
    LimitType limitType() default LimitType.DEFAULT;
}