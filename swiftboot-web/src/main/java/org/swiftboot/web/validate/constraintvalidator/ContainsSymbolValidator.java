package org.swiftboot.web.validate.constraintvalidator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;
import org.swiftboot.web.validate.constraint.ContainsSymbol;


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
        return StringUtils.containsAny(pwd, ";',./~!@#$%^&*()_+|{}:\"<>?[]");
    }
}
