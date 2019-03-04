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
 * 检验字符串中是否含有符号字符
 *
 * @author swiftech 2018-01-18
 **/
@Documented
@Constraint(validatedBy = {ContainsSymbolValidator.class})
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
@Retention(RUNTIME)
public @interface ContainsSymbol {

    String message() default "{org.swiftboot.constraints.ContainsSymbol.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
