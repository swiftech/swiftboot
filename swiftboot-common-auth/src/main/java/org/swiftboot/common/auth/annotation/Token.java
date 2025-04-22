package org.swiftboot.common.auth.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @since 3.0
 */
@Target({ElementType.PARAMETER})
@Retention(RUNTIME)
@Documented
public @interface Token {
}
