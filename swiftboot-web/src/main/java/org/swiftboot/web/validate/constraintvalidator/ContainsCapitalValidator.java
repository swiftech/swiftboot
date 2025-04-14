package org.swiftboot.web.validate.constraintvalidator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;
import org.swiftboot.web.validate.constraint.ContainsCapital;


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
        return StringUtils.containsAny(pwd, "ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    }
}
