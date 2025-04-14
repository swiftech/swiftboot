package org.swiftboot.web.validate.constraintvalidator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;
import org.swiftboot.web.validate.constraint.DecimalString;


/**
 * @author swiftech
 * @since 1.2
 */
public class DecimalStringValidator implements ConstraintValidator<DecimalString, String> {

    private DecimalString onlyNumber;

    @Override
    public void initialize(DecimalString constraintAnnotation) {
        this.onlyNumber = constraintAnnotation;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // this validator doesn't validate 'null' value
        return value == null || (StringUtils.isNotBlank(value) && value.matches("^[0-9]*\\.?[0-9]*$"));
    }
}
