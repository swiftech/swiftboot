package org.swiftboot.common.auth.annotation;

import io.swagger.v3.oas.annotations.Parameter;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Inject token expire time to controller's method parameter with this annotation.
 *
 * @author swiftech
 * @see org.swiftboot.common.auth.aop.JwtArgumentResolver
 * @since 2.1
 */
@Target({ElementType.PARAMETER})
@Retention(RUNTIME)
@Documented
@Parameter(hidden = true)
public @interface ExpireTime {
}
