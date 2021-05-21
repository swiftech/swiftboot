package org.swiftboot.sheet.meta;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.swiftboot.sheet.exception.NotSupportedExpressionException;
import org.swiftboot.sheet.util.LetterUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.*;
import static org.swiftboot.sheet.util.CalculateUtils.powForExcel;

/**
 * Translate expression to area of cells in sheet.
 * <p>
 * Expression rules example:
 * <p>
 * E2 or 2E means single cell in row 2 column 5.
 * E2:E3, 2:E3, E2:3, 2E:3 or 2:3E means cells from row 2 to 3 in column 5.
 * 2E:H or E:H2 means cells from column 5 to 8 in row 2.
 * E2-3 or 2E-3 means cells from column 5 to column 8 in row 2.
 * E2|3 or 2E|3 means cells from rom 2 to 3 in column 5.
 * E2:H3 means cells from row 2 to 3 and from column 5 to 8.
 * E2:E? means export specified rows starts from row 2 in column 5.
 * E2:?2 means export specified columns starts from column 5 in row 2.
 * E2:? means export specified rows and columns starts from row 2 column 5.
 *
 * <p>Sheet</p>
 * $'sheet.0'.E2:E3 means cells from rom 2 to 3 in column 5 of the sheet names 'sheet.0'
 * $'sheet.1'.E2 means single cell in row 2 column 5 of the sheet names 'sheet.1'
 *
 * @author allen
 */
public class Translator {
    private final Logger log = LoggerFactory.getLogger(Translator.class);

    private int[] sequences = {26 * 26 * 26, 26 * 26, 26, 1};

    /**
     * Translate expression to {@code Area}.
     *
     * @param exp
     * @return
     */
    public Area toArea(String exp) {
        Expression expression = new Expression(exp);
        Area area;
        if (expression.isSinglePosition()) {
            area = new Area(this.toSinglePosition(expression.getCellsExp()));
        }
        else if (expression.isRange()) {
            area = this.freeRange(expression.splitAsFreeRange());
        }
        else if (expression.isVerticalRange() || expression.isHorizontalRange()) {
            area = this.lineRange(expression);
        }
        else {
            throw new RuntimeException("Don't know how to handle this expression: " + exp);
        }
        if (StringUtils.isNotBlank(expression.getSheetName())) {
            area.setSheetId(new SheetId(expression.getSheetName()));
        }
        return area;
    }

    /**
     * @param startEnd expression array for start and end positions
     * @return
     */
    private Area freeRange(String[] startEnd) {
        if (startEnd == null || startEnd.length != 2) {
            throw new NotSupportedExpressionException(Arrays.toString(startEnd));
        }
        String startExp = startEnd[0];
        String endExp = startEnd[1];
        String[] seg1 = splitByCharacterType(startExp.toUpperCase());
        String[] seg2 = splitByCharacterType(endExp.toUpperCase());
        String[] all = ArrayUtils.addAll(seg1, seg2);
        List<Integer> colIndexes = new ArrayList<>();
        List<Integer> rowIndexes = new ArrayList<>();
        boolean hasUncertainSize = false;
        for (String e : all) {
            if (isNumeric(e)) {
                rowIndexes.add(NumberUtils.toInt(e) - 1);
            }
            else if (isAlpha(e)) {
                colIndexes.add(expToIndex(e));
            }
            else if (contains(e, '?')) {
                hasUncertainSize = true;
            }
        }
        if (colIndexes.isEmpty() || rowIndexes.isEmpty()) {
            throw new NotSupportedExpressionException(Arrays.toString(startEnd));
        }
        colIndexes.sort(Integer::compareTo);
        rowIndexes.sort(Integer::compareTo);

        Position startPos = new Position(rowIndexes.get(0), colIndexes.get(0));

        Integer endRow = rowIndexes.size() > 1
                ? rowIndexes.get(1)
                : (hasUncertainSize ? null : rowIndexes.get(0));
        Integer endColumn = colIndexes.size() > 1
                ? colIndexes.get(1)
                : hasUncertainSize ? null : colIndexes.get(0);
        Position endPos = new Position(endRow, endColumn);
        return new Area(startPos, endPos);
    }

    /**
     * @param expression
     * @return
     */
    private Area lineRange(Expression expression) {
        boolean isVertical = expression.isVerticalRange();
        boolean isHorizontal = expression.isHorizontalRange();
        String[] startEnd = isVertical ? expression.splitAsVerticalRange() : expression.splitAsHorizontalRange();
        String startExp = startEnd[0];
        String endExp = startEnd[1];
        int rangeSize;
        // Determine start position
        Position startPos;
        if (StringUtils.isNumeric(startExp)) {
            rangeSize = NumberUtils.toInt(startExp);
            startPos = this.toSinglePosition(endExp);
        }
        else {
            startPos = this.toSinglePosition(startExp);
            rangeSize = NumberUtils.toInt(endExp);
        }
        // Determine end position
        Position endPos;
        if (isHorizontal) {
            endPos = new Position(startPos.getRow(), startPos.getColumn() + rangeSize);
        }
        else {
            endPos = new Position(startPos.getRow() + rangeSize, startPos.getColumn());
        }
        return new Area(startPos, endPos);
    }

    /**
     * Translate single position expression.
     *
     * @param exp
     * @return
     */
    public Position toSinglePosition(String exp) {
        if (isBlank(exp)) {
            return null;
        }
        String[] split = splitByCharacterType(exp.toUpperCase());
        return toPosition(split);
    }


    /**
     * Translate parts of expression to {@code Position}.
     *
     * @param expParts expression parts for single position expression.
     * @return
     */
    Position toPosition(String[] expParts) {
        Integer row = null;
        Integer column = null;
        String left = expParts[0];
        String right = expParts.length > 1 ? expParts[1] : null;
        if (isNumeric(left)) {
            row = NumberUtils.toInt(left) - 1;
            if (isNotBlank(right)) {
                if (!"?".equals(right)) {
                    column = expToIndex(right);
                }
            }
        }
        else if (isAlpha(left)) {
            column = expToIndex(left);
            if (isNotBlank(right)) {
                if (!"?".equals(right)) {
                    row = NumberUtils.toInt(right) - 1;
                }
            }
        }
        else if ("?".equals(left)) {
            if (isNotBlank(right)) {
                if (isNumeric(right)) {
                    row = NumberUtils.toInt(right) - 1;
                }
                else if (isAlpha(right)) {
                    column = expToIndex(right);
                }
                else {
                    throw new RuntimeException("");
                }
            }
        }
        return new Position(row, column);
    }


    /**
     * Parse column expression like 'BA' to actual column number.
     *
     * @param exp
     * @return
     */
    int expToIndex(String exp) {
        if (exp.length() > 4) {
            throw new RuntimeException("Length must be less than 4");
        }
        int ret = 0;
        String reversed = reverse(exp);
        char[] charArray = reversed.toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            if (i == 0) {
                ret += LetterUtils.letterToIndex(charArray[i]);
            }
            else {
                ret += ((LetterUtils.letterToIndex(charArray[i]) + 1) * powForExcel(i));
            }
        }
        return ret;
    }


    /**
     * TBD
     *
     * @param position
     * @return
     */
    String toExpression(Position position) {
        return indexToExp(position.getColumn()) + (position.getRow() + 1);
    }

    /**
     * TBD
     *
     * @param index
     * @return
     */
    String indexToExp(int index) {
        int x = index;
        if (x < 0) {
            return null;
        }
        int cutoff = 0;
        List<Integer> xxx = new ArrayList<>();
        for (int s : sequences) {
            int letterIdx = (x - cutoff) / s;
            if (letterIdx < 0) {
                continue;
            }
            xxx.add(letterIdx);
            cutoff += letterIdx * s;
        }
        log.debug(String.format("%d - %s%n", index, StringUtils.join(xxx)));
        StringBuilder ret = new StringBuilder();
        boolean isUpgrade = false;
        // from lowest to highest
        for (int i = xxx.size() - 1; i >= 0; i--) {
            int idx = xxx.get(i);
            if (i == xxx.size() - 1) { // lowest letter
                int letterNum = idx + 1;
                if (letterNum > 26) {
                    isUpgrade = true;
                }
                if (letterNum == 26) {
                    ret.append('Z');
                }
                else {
                    ret.append(LetterUtils.numberToLetter(letterNum));
                }
            }
            else {
                int letterNum = idx;
                if (isUpgrade) {
                    letterNum = letterNum + 1;
                    // continue upgrade if more than 26
                    if (letterNum <= 6) {
                        isUpgrade = false;
                    }
                }
                if (letterNum == 0) {
                    continue;
                }
                ret.append(LetterUtils.numberToLetter(letterNum));
            }
        }
        ret.reverse();
        String s = ret.toString();
        log.debug(s);
        return s;
    }
}
