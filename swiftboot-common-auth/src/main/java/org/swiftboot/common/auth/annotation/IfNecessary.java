package org.swiftboot.common.auth.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * This annotation is used in conjunction with other annotations.
 * Once added, JwtArgumentResolver will attempt to retrieve the necessary information from the access token or session.
 * If this fails, an `AuthenticationException` exception will be thrown (ultimately returning a 401 Unauthorized error).
 *
 * @see org.swiftboot.common.auth.aop.JwtArgumentResolver
 * @see UserId
 * @see UserName
 * @see ExpireTime
 * @see Token
 * @see Addition
 * @since 3.1.0
 */
@Target({ElementType.PARAMETER})
@Retention(RUNTIME)
public @interface IfNecessary {
}
