package org.swiftboot.web.validate.constraintvalidator;

import org.apache.commons.lang3.StringUtils;
import org.swiftboot.web.validate.constraint.ContainsSymbol;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author swiftech
 **/
public class ContainsSymbolValidator implements ConstraintValidator<ContainsSymbol, String> {

    private ContainsSymbol constraint;

    @Override
    public void initialize(ContainsSymbol pwd) {
        this.constraint = pwd;
    }

    @Override
    public boolean isValid(String pwd, ConstraintValidatorContext constraintValidatorContext) {
        if (!StringUtils.containsAny(pwd, ";',./~!@#$%^&*()_+|{}:\"<>?[]")) {
            return false;
        }
        return true;
    }
}
