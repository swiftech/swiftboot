package org.swiftboot.web.validate;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 转换验证结果
 *
 * @author swiftech
 * @see ValidateResultAspect
 */
@Target({METHOD})
@Retention(RUNTIME)
@Documented
public @interface ConvertValidateResult {
}
