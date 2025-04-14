package org.swiftboot.web.validate.constraint;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.swiftboot.web.validate.constraintvalidator.DecimalStringValidator;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 浮点数字符串
 *
 * @author swiftech
 * @since 1.2
 */
@Documented
@Constraint(validatedBy = {DecimalStringValidator.class})
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
@Retention(RUNTIME)
public @interface DecimalString {

    String message() default "{org.swiftboot.constraints.DecimalString.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
