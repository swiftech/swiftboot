package org.swiftboot.web.validate.constraint;

import org.swiftboot.web.validate.constraintvalidator.ContainsSymbolValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 字符串中全部都是数字
 *
 * @author swiftech
 */
@Documented
@Constraint(validatedBy = {ContainsSymbolValidator.class})
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
@Retention(RUNTIME)
public @interface OnlyNumber {


    String message() default "{org.swiftboot.constraints.OnlyNumber.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
