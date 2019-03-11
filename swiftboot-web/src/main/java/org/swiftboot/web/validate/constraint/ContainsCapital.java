package org.swiftboot.web.validate.constraint;

import org.swiftboot.web.validate.constraintvalidator.ContainsCapitalValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 检验字符串中是否含有大写字符
 *
 * @author swiftech
 **/
@Documented
@Constraint(validatedBy = {ContainsCapitalValidator.class})
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
@Retention(RUNTIME)
public @interface ContainsCapital {

    String message() default "{org.swiftboot.constraints.ContainsCapital.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
