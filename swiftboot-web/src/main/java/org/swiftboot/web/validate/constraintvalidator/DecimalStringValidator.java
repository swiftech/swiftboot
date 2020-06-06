package org.swiftboot.web.validate.constraintvalidator;

import org.swiftboot.web.validate.constraint.DecimalString;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

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
        return value.matches("^[0-9]*\\.?[0-9]*$");
    }
}
