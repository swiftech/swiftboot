package org.swiftboot.web.validate.constraint;

import org.swiftboot.web.validate.constraintvalidator.ContainsNumberValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 校验字符串是否含有数字
 * @author swiftech 2018-01-18
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
