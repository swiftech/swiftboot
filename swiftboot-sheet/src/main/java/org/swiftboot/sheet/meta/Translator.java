package org.swiftboot.sheet.meta;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
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
 * Expression rules:
 * <p>
 * E2 or 2E means single cell in row 2 column 5.
 * E2:E3, 2:E3, E2:3, 2E:3 or 2:3E means cells from row 2 to 3 in column 5.
 * 2E:H or E:H2 means cells from column 5 to 8 in row 2.
 * E2:H3 means cells from row 2 to 3 and from column 5 to 8.
 * E2-3 or 2E-3 means cells from column 5 to column 8 in row 2.
 * E2|3 or 2E|3 means cells from rom 2 to 3 in column 5.
 *
 * @author allen
 */
public class Translator {

    public Area toArea(String exp) {
        Expression expression = new Expression(exp);
        if (expression.isSinglePosition()) {
            return new Area(this.toSinglePosition(exp));
        }
        else if (expression.isFreeRange()) {
            return freeRange(expression.splitAsFreeRange());
        }
        else if (expression.isVerticalRange() || expression.isHorizontalRange()) {
            return this.lineRange(expression);
        }
        else {
            throw new RuntimeException("Don't know how to handle");
        }
    }

    /**
     * TODO 需要支持 ? 的形式的表达
     * @param exp2
     * @return
     */
    private Area freeRange(String[] exp2) {
        if (exp2 == null || exp2.length != 2) {
            throw new NotSupportedExpressionException(Arrays.toString(exp2));
        }
        String[] seg1 = splitByCharacterType(exp2[0].toUpperCase());
        String[] seg2 = splitByCharacterType(exp2[1].toUpperCase());
        String[] all = ArrayUtils.addAll(seg1, seg2);
        List<String> letters = new ArrayList<>();
        List<String> numbers = new ArrayList<>();
        for (String e : all) {
            if (isNumeric(e)) {
                numbers.add(e);
            }
            else {
                letters.add(e);
            }
        }
        if (letters.isEmpty() || numbers.isEmpty()) {
            throw new NotSupportedExpressionException(Arrays.toString(exp2));
        }
        letters.sort(String::compareTo);
        numbers.sort(String::compareTo);

        Position startPos = new Position(
                NumberUtils.toInt((numbers.get(0))) - 1,
                expToIndex(letters.get(0))
        );
        Position endPos = new Position(
                NumberUtils.toInt((numbers.size() > 1 ? numbers.get(1) : numbers.get(0))) - 1,
                expToIndex(letters.size() > 1 ? letters.get(1) : letters.get(0))
        );
        return new Area(startPos, endPos);
    }


    private Area lineRange(Expression expression) {
        boolean isVertical = expression.isVerticalRange();
        boolean isHorizontal = expression.isHorizontalRange();
        String[] exp2 = isVertical ? expression.splitAsVerticalRange() : expression.splitAsHorizontalRange();
        int len;
        Position startPos;
        if (StringUtils.isNumeric(exp2[0])) {
            len = NumberUtils.toInt(exp2[0]);
            startPos = this.toSinglePosition(exp2[1]);
        }
        else {
            startPos = this.toSinglePosition(exp2[0]);
            len = NumberUtils.toInt(exp2[1]);
        }
        Position endPos;
        if (isHorizontal) {
            endPos = new Position(startPos.getRow(), startPos.getColumn() + len);
        }
        else {
            endPos = new Position(startPos.getRow() + len, startPos.getColumn());
        }
        return new Area(startPos, endPos);
    }

    /**
     * translate single position expression.
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
     * TBD
     *
     * @param position
     * @return
     */
    String toExpression(Position position) {
        return indexToExp(position.getColumn()) + (position.getRow() + 1);
    }

    /**
     * @param split
     * @return
     */
    Position toPosition(String[] split) {
        Integer row = null;
        Integer column = null;
        String left = split[0];
        String right = split.length > 1 ? split[1] : null;
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
            if (isNotBlank(right)){
                if (isNumeric(right)){
                    row = NumberUtils.toInt(right) - 1;
                }
                else if (isAlpha(right)){
                    column = expToIndex(right);
                }
                else {
                    throw new RuntimeException("");
                }
            }
        }
        return new Position(row, column);
    }

    String indexToExp(int index) {
        System.out.println();
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
        System.out.printf("%d - %s%n", index, StringUtils.join(xxx));
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
        System.out.println(s);
        return s;
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


    private int[] sequences = {26 * 26 * 26, 26 * 26, 26, 1};

}
