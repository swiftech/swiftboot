package org.swiftboot.web.validate.constraintvalidator;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.swiftboot.web.validate.constraint.PhoneNo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 验证手机号码是否为“1”开头的11位数字
 *
 * @author swiftech
 */
public class PhoneNoValidator implements ConstraintValidator<PhoneNo, String> {

    private Logger logger = LoggerFactory.getLogger(PhoneNoValidator.class);

    @Override
    public void initialize(PhoneNo phoneNo) {
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        logger.debug("验证手机号是否正确...");
        if (StringUtils.isBlank(s)) {
            return true;
        }
        return s.trim().length() == 11
                && NumberUtils.isDigits(s.trim())
                && StringUtils.startsWith(s.trim(), "1");
    }
}
