package org.swiftboot.web.validate.constraint;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.swiftboot.web.validate.constraintvalidator.ContainsNumberValidator;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 校验字符串是否含有数字
 *
 * @author swiftech
 **/
@Documented
@Constraint(validatedBy = {ContainsNumberValidator.class})
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
@Retention(RUNTIME)
public @interface ContainsNumber {

    String message() default "{org.swiftboot.constraints.ContainsNumber.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
