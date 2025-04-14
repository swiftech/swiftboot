package org.swiftboot.web.validate.constraintvalidator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;
import org.swiftboot.web.validate.constraint.ContainsNumber;


/**
 * @author swiftech
 **/
public class ContainsNumberValidator implements ConstraintValidator<ContainsNumber, String> {

    private ContainsNumber constraint;

    @Override
    public void initialize(ContainsNumber pwd) {
        this.constraint = pwd;
    }

    @Override
    public boolean isValid(String pwd, ConstraintValidatorContext constraintValidatorContext) {
        return StringUtils.containsAny(pwd, "1234567890");
    }
}
