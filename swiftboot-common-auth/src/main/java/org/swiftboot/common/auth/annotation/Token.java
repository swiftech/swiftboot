package org.swiftboot.common.auth.annotation;

import io.swagger.v3.oas.annotations.Parameter;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Inject user token to controller's method parameter with this annotation.
 *
 * @see org.swiftboot.common.auth.aop.JwtArgumentResolver
 * @since 3.0
 */
@Target({ElementType.PARAMETER})
@Retention(RUNTIME)
@Documented
@Parameter(hidden = true)
public @interface Token {
}
