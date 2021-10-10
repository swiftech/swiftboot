package org.swiftboot.web.validate.constraint;

import org.swiftboot.web.validate.constraintvalidator.PhoneNoValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 校验中国地区11位手机号码
 *
 * @author swiftech
 */
@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = PhoneNoValidator.class)
@Documented
public @interface PhoneNo {

    String message() default "{org.swiftboot.constraints.PhoneNo.message}";

    String prefix();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
