package org.swiftboot.sheet.exception;

/**
 * @author allen
 */
public class NotSupportedExpressionException extends RuntimeException {

    private String expression;

    public NotSupportedExpressionException(String expression) {
        super(String.format("Not supported expression: %s", expression));
    }
}
