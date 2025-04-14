package org.swiftboot.web.validate.constraint;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.swiftboot.web.validate.constraintvalidator.OnlyNumberValidator;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 字符串中全部都是数字（不包括浮点数）
 *
 * @author swiftech
 * @since 1.1.1
 */
@Documented
@Constraint(validatedBy = {OnlyNumberValidator.class})
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
@Retention(RUNTIME)
public @interface OnlyNumber {


    String message() default "{org.swiftboot.constraints.OnlyNumber.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
