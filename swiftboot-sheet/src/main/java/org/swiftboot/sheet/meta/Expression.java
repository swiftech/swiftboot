package org.swiftboot.sheet.meta;

import org.apache.commons.lang3.StringUtils;

import static org.apache.commons.lang3.StringUtils.*;

/**
 * @author allen
 */
public class Expression {

    private final String expression;

    public Expression(String expression) {
        this.expression = expression;
    }

    public String getSheetName() {
        // TODO
        return substringBetween(expression, "$'", "'.");
    }

    public String getAreaExpression() {
        return substringAfter(expression, ".");
    }

    public boolean isRange() {
        return contains(expression, ":");
    }

    public boolean isHorizontalRange() {
        return contains(expression, "-");
    }

    public boolean isVerticalRange() {
        return contains(expression, "|");
    }

    public boolean isSinglePosition() {
        return !isRange() && !isHorizontalRange() && !isVerticalRange();
    }

    public String[] splitAsFreeRange() {
        String[] split = split(this.expression, ':');
        if (split == null || split.length != 2) {
            throw new RuntimeException(String.format("Illegal expression: %s", this.expression));
        }
        return split;
    }

    public String[] splitAsHorizontalRange() {
        String[] split = split(this.expression, '-');
        if (split == null || split.length != 2 || !StringUtils.isNumeric(split[1])) {
            throw new RuntimeException(String.format("Illegal expression: %s", this.expression));
        }
        return split;
    }

    public String[] splitAsVerticalRange() {
        String[] split = split(this.expression, '|');
        if (split == null || split.length != 2 || !StringUtils.isNumeric(split[1])) {
            throw new RuntimeException(String.format("Illegal expression: %s", this.expression));
        }
        return split;
    }

    public String getExpression() {
        return expression;
    }
}
