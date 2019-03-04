package org.swiftboot.web.validate.constraintvalidator;

import org.apache.commons.lang3.StringUtils;
import org.swiftboot.web.validate.constraint.ContainsCapital;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author swiftech
 **/
public class ContainsCapitalValidator implements ConstraintValidator<ContainsCapital, String> {

    private ContainsCapital constraint;

    @Override
    public void initialize(ContainsCapital pwd) {
        this.constraint = pwd;
    }

    @Override
    public boolean isValid(String pwd, ConstraintValidatorContext constraintValidatorContext) {
        if (!StringUtils.containsAny(pwd, "ABCDEFGHIJKLMNOPQRSTUVWXYZ")) {
            return false;
        }
        return true;
    }
}
