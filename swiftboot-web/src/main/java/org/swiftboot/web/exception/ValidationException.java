package org.swiftboot.web.exception;


import org.swiftboot.web.validate.ValidationResult;

/**
 * 表单验证未通过时抛出异常
 *
 * @author swiftech
 **/
public class ValidationException extends RuntimeException {

    private ValidationResult validationResult;

    public ValidationException(ValidationResult validationResult) {
        this.validationResult = validationResult;
    }

    public ValidationException(String message, ValidationResult validationResult) {
        super(message);
        this.validationResult = validationResult;
    }

    public ValidationResult getValidationResult() {
        return validationResult;
    }

    public void setValidationResult(ValidationResult validationResult) {
        this.validationResult = validationResult;
    }
}
