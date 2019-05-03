package org.swiftboot.web.validate;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 转换验证结果
 *
 * @author swiftech
 * @see ValidateResultAspect
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RUNTIME)
@Documented
public @interface ConvertValidateResult {
}
