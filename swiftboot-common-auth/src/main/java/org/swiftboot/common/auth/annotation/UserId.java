package org.swiftboot.common.auth.annotation;

import io.swagger.v3.oas.annotations.Parameter;
import org.swiftboot.common.auth.aop.JwtArgumentResolver;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author swiftech
 * @see JwtArgumentResolver
 * @since 2.1
 */
@Target({ElementType.PARAMETER})
@Retention(RUNTIME)
@Documented
@Parameter(hidden = true)
public @interface UserId {
}
