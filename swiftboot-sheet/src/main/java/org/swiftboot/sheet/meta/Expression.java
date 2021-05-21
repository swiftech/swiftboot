package org.swiftboot.sheet.meta;

import org.apache.commons.lang3.StringUtils;

import static org.apache.commons.lang3.StringUtils.*;

/**
 * Expression like Excel's cell expression.
 * More specific parsing will be in {@link Translator}
 *
 * @author allen
 * @see Translator
 */
public class Expression {

    private String sheetName = EMPTY;
    private String cellsExp;

    public Expression(String expression) {
        String[] split = splitByWholeSeparator(expression, "'.");
        if (split != null) {
            if (split.length == 2) {
                this.sheetName = stripStart(split[0], "$'");
                this.cellsExp = split[1];
            }
            else {
                this.cellsExp = expression;
            }
        }
    }

    public String getSheetName() {
        return sheetName;
    }

    public boolean isRange() {
        return contains(cellsExp, ":");
    }

    public boolean isHorizontalRange() {
        return contains(cellsExp, "-");
    }

    public boolean isVerticalRange() {
        return contains(cellsExp, "|");
    }

    public boolean isSinglePosition() {
        return !isRange() && !isHorizontalRange() && !isVerticalRange();
    }

    public String[] splitAsFreeRange() {
        String[] split = split(this.cellsExp, ':');
        if (split == null || split.length != 2) {
            throw new RuntimeException(String.format("Illegal expression: %s", this.cellsExp));
        }
        return split;
    }

    public String[] splitAsHorizontalRange() {
        String[] split = split(this.cellsExp, '-');
        if (split == null || split.length != 2 || !StringUtils.isNumeric(split[1])) {
            throw new RuntimeException(String.format("Illegal expression: %s", this.cellsExp));
        }
        return split;
    }

    public String[] splitAsVerticalRange() {
        String[] split = split(this.cellsExp, '|');
        if (split == null || split.length != 2 || !StringUtils.isNumeric(split[1])) {
            throw new RuntimeException(String.format("Illegal expression: %s", this.cellsExp));
        }
        return split;
    }

    public String getCellsExp() {
        return cellsExp;
    }
}
