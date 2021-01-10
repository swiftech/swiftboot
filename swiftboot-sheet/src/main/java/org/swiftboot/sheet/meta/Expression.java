package org.swiftboot.sheet.meta;

import org.apache.commons.lang3.StringUtils;

import static org.apache.commons.lang3.StringUtils.*;

/**
 * @author allen
 */
public class Expression {

    private String expression;

    public Expression(String expression) {
        this.expression = expression;
    }

    public boolean isFreeRange() {
        return contains(expression, ":");
    }

    public boolean isHorizontalRange() {
        return contains(expression, "-");
    }

    public boolean isVerticalRange() {
        return contains(expression, "|");
    }

    public boolean isSinglePoint() {
        return !isFreeRange() && !isHorizontalRange() && !isVerticalRange();
    }

    public String[] splitAsFreeRange() {
        String[] split = split(this.expression, ':');
        if (split == null || split.length != 2) {
            throw new RuntimeException("Illegal expression: " + this.expression);
        }
        return split;
    }

    public String[] splitAsHorizontalRange() {
        String[] split = split(this.expression, '-');
        if (split == null || split.length != 2 || !StringUtils.isNumeric(split[1])) {
            throw new RuntimeException("Illegal expression: " + this.expression);
        }
        return split;
    }

    public String[] splitAsVerticalRange() {
        String[] split = split(this.expression, '|');
        if (split == null || split.length != 2 || !StringUtils.isNumeric(split[1])) {
            throw new RuntimeException("Illegal expression: " + this.expression);
        }
        return split;
    }

    public String getExpression() {
        return expression;
    }
}
