package org.swiftboot.auth.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author swiftech
 * @since 2.1
 */
@Target({ElementType.PARAMETER})
@Retention(RUNTIME)
@Documented
public @interface UserName {
}
