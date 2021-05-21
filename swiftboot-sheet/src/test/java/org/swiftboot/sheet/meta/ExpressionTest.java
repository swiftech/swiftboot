package org.swiftboot.sheet.meta;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 *
 * @author allen
 * @see Expression
 */
class ExpressionTest {

    static Expression expSingle;
    static Expression expHorizontal;
    static Expression expHorizontal2;
    static Expression expVertical;
    static Expression expVertical2;
    static Expression expMatrix;
    static Expression expFreeSize;
    static Expression expSheet;
    static Expression expSheetSingle;

    @BeforeAll
    public static void setup() {
        expSingle = new Expression("a1");
        expHorizontal = new Expression("a1:a2");
        expHorizontal2 = new Expression("a1-2");
        expVertical = new Expression("a1:b1");
        expVertical2 = new Expression("a1|2");
        expMatrix = new Expression("a1:b2");
        expFreeSize = new Expression("a1:?");
        expSheet = new Expression("$'sheet.0'.a1:b1");
        expSheetSingle = new Expression("$'sheet.0'.a1");
    }

    @Test
    void testSheet() {
        Assertions.assertEquals("sheet.0", expSheet.getSheetName());
        Assertions.assertEquals("a1:b1", expSheet.getCellsExp());

        Assertions.assertEquals("sheet.0", expSheetSingle.getSheetName());
        Assertions.assertEquals("a1", expSheetSingle.getCellsExp());
    }

    @Test
    void isFreeRange() {
        Assertions.assertFalse(expSingle.isRange());
        Assertions.assertTrue(expHorizontal.isRange());
        Assertions.assertFalse(expHorizontal2.isRange());
        Assertions.assertTrue(expVertical.isRange());
        Assertions.assertFalse(expVertical2.isRange());
        Assertions.assertTrue(expMatrix.isRange());
        Assertions.assertTrue(expFreeSize.isRange());
        Assertions.assertTrue(expSheet.isRange());
    }

    @Test
    void isHorizontalRange() {
        Assertions.assertFalse(expSingle.isHorizontalRange());
        Assertions.assertFalse(expHorizontal.isHorizontalRange());
        Assertions.assertTrue(expHorizontal2.isHorizontalRange());
        Assertions.assertFalse(expVertical.isHorizontalRange());
        Assertions.assertFalse(expVertical2.isHorizontalRange());
        Assertions.assertFalse(expMatrix.isHorizontalRange());
        Assertions.assertFalse(expFreeSize.isHorizontalRange());
    }

    @Test
    void isVerticalRange() {
        Assertions.assertFalse(expSingle.isVerticalRange());
        Assertions.assertFalse(expHorizontal.isVerticalRange());
        Assertions.assertFalse(expHorizontal2.isVerticalRange());
        Assertions.assertFalse(expVertical.isVerticalRange());
        Assertions.assertTrue(expVertical2.isVerticalRange());
        Assertions.assertFalse(expMatrix.isVerticalRange());
        Assertions.assertFalse(expFreeSize.isVerticalRange());
    }

    @Test
    void isSinglePosition() {
        Assertions.assertTrue(expSingle.isSinglePosition());
        Assertions.assertFalse(expHorizontal.isSinglePosition());
        Assertions.assertFalse(expHorizontal2.isSinglePosition());
        Assertions.assertFalse(expVertical.isSinglePosition());
        Assertions.assertFalse(expVertical2.isSinglePosition());
        Assertions.assertFalse(expMatrix.isSinglePosition());
        Assertions.assertFalse(expFreeSize.isSinglePosition());
    }

    @Test
    void splitAsFreeRange() {
        Assertions.assertFalse(expSingle.isRange());
        Assertions.assertTrue(expHorizontal.isRange());
        Assertions.assertFalse(expHorizontal2.isRange());
        Assertions.assertTrue(expVertical.isRange());
        Assertions.assertFalse(expVertical2.isRange());
        Assertions.assertTrue(expMatrix.isRange());
        Assertions.assertTrue(expFreeSize.isRange());
    }

    @Test
    void splitAsHorizontalRange() {
        Assertions.assertArrayEquals(new String[]{"a1","2"}, expHorizontal2.splitAsHorizontalRange());
    }

    @Test
    void splitAsVerticalRange() {
        Assertions.assertArrayEquals(new String[]{"a1","2"}, expVertical2.splitAsVerticalRange());
    }
}