package org.swiftboot.web.validate.constraintvalidator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;
import org.swiftboot.web.validate.constraint.OnlyNumber;


/**
 *
 * @author swiftech
 * @since 1.1.1
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
