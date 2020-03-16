package org.swiftboot.web.validate.constraintvalidator;

import org.apache.commons.lang3.StringUtils;
import org.swiftboot.web.validate.constraint.OnlyNumber;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 *
 * @author swiftech
 */
public class OnlyNumberValidator implements ConstraintValidator<OnlyNumber, String> {

    private OnlyNumber onlyNumber;

    @Override
    public void initialize(OnlyNumber constraintAnnotation) {
        this.onlyNumber = constraintAnnotation;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return StringUtils.isNumeric(value);
    }
}
