package org.swiftboot.sheet.meta;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test {@code Translator}
 *
 * @author swiftech
 * @see Translator
 */
class TranslatorTest {

    @Test
    public void testSimpleExpression() {
        Translator translator = new Translator();

        Assertions.assertEquals(0, translator.expToIndex("a"));
        Assertions.assertEquals(26, translator.expToIndex("aa"));
        Assertions.assertEquals(27, translator.expToIndex("ab"));
        Assertions.assertEquals(130, translator.expToIndex("ea"));
        Assertions.assertEquals(135, translator.expToIndex("ef"));
        Assertions.assertEquals(702, translator.expToIndex("aaa"));
        Assertions.assertEquals(18278, translator.expToIndex("aaaa"));
        System.out.println();

        Assertions.assertEquals(new Position(0, 1), translator.toSinglePosition("B1"));
        Assertions.assertEquals(new Position(0, 0), translator.toSinglePosition("a1"));
        Assertions.assertEquals(new Position(16, 135), translator.toSinglePosition("Ef17"));

        Assertions.assertEquals(new Position(0, 1), translator.toSinglePosition("1B"));
        Assertions.assertEquals(new Position(0, 0), translator.toSinglePosition("1a"));
        Assertions.assertEquals(new Position(16, 135), translator.toSinglePosition("17Ef"));

        Assertions.assertEquals(new Position(null, 4), translator.toSinglePosition("E?"));
        Assertions.assertEquals(new Position(null, 4), translator.toSinglePosition("?E"));
        Assertions.assertEquals(new Position(2, null), translator.toSinglePosition("?3"));
        Assertions.assertEquals(new Position(2, null), translator.toSinglePosition("3?"));
        Assertions.assertEquals(new Position(null, null), translator.toSinglePosition("?"));
        System.out.println();

        // too long
        Assertions.assertThrows(Exception.class, () -> translator.expToIndex("aaaaa"));

        Area withSingle = new Area(new Position(0, 0), null);
        Assertions.assertEquals(withSingle, translator.toArea("a1"));

    }

    /**
     * Test Translator.toArea()
     */
    @Test
    public void testComplexExpression() {
        Translator translator = new Translator();

        // Matrix
        Area cube = new Area(0, 0, 1, 1);
        Assertions.assertEquals(cube, translator.toArea("a1:b2"));
        Assertions.assertEquals(cube, translator.toArea("A1:B2"));
        Assertions.assertEquals(cube, translator.toArea("1A:B2"));
        Assertions.assertEquals(cube, translator.toArea("1A:2B"));
        Assertions.assertEquals(cube, translator.toArea("A1:2B"));
        Assertions.assertEquals(cube, translator.toArea("A2:B1"));
        Assertions.assertEquals(cube, translator.toArea("B1:A2"));// reverse

        // Horizontal line
        Area horizontalLine = new Area(0, 0, 0, 9);
        Assertions.assertEquals(horizontalLine, translator.toArea("A1:J1"));
        Assertions.assertEquals(horizontalLine, translator.toArea("A1:J"));
        Assertions.assertEquals(horizontalLine, translator.toArea("A:J1"));

        Assertions.assertEquals(new Area(2, 2, 2, 3), translator.toArea("c3:d3"));

        Assertions.assertEquals(horizontalLine, translator.toArea("A1-9"));
        Assertions.assertThrows(Exception.class, () -> translator.toArea("A1-A9"));

        // Vertical line
        Area verticalLine = new Area(0, 0, 9, 0);
        Assertions.assertEquals(verticalLine, translator.toArea("A1:A10"));
        Assertions.assertEquals(verticalLine, translator.toArea("1A:10A"));
        Assertions.assertEquals(verticalLine, translator.toArea("A1:10"));
        Assertions.assertEquals(verticalLine, translator.toArea("1:10A"));
        Assertions.assertEquals(verticalLine, translator.toArea("1:A10"));
        Assertions.assertEquals(verticalLine, translator.toArea("A1|9"));
        Assertions.assertEquals(verticalLine, translator.toArea("A10:A1"));// Reverse
    }

    /**
     * Test uncertain data size ( only for export)
     */
    @Test
    public void testUncertainDataSize() {
        Translator translator = new Translator();
        // horizontal line
        Area horizontalLineUncertain = new Area(0, 0, 0, null);
        Assertions.assertEquals(horizontalLineUncertain, translator.toArea("A1:?1"));

        Assertions.assertThrows(RuntimeException.class, () -> translator.toArea("?1:?1"));
//        Assertions.assertThrows(RuntimeException.class, () -> translator.toArea("A?:?1")); // TODO should be forbidden

        // Vertical line
        Area verticalLineUncertain = new Area(0, 0, null, 0);
        Assertions.assertEquals(verticalLineUncertain, translator.toArea("A1:A?"));

        Assertions.assertThrows(RuntimeException.class, () -> translator.toArea("a?:a?"));
//        Assertions.assertThrows(RuntimeException.class, () -> translator.toArea("?1:a?"));// TODO should be forbidden

        // Matrix
        Area matrix = new Area(new Position(0, 0), new Position(null, null));
        Assertions.assertEquals(matrix, translator.toArea("a1:?"));
        Assertions.assertEquals(matrix, translator.toArea("a1:??"));
    }

    /**
     * Test illegal expressions
     */
    @Test
    public void testIllegalExpression() {
        Translator translator = new Translator();
        Assertions.assertThrows(Exception.class, () -> translator.toArea(""));
        Assertions.assertThrows(Exception.class, () -> translator.toArea("A:J"));
        Assertions.assertThrows(Exception.class, () -> translator.toArea("1:10"));
    }

    @Test
    public void testWithSheet() {
        Translator translator = new Translator();
        // the expression without sheet name should be translated to area with default sheet id.
        Area area = translator.toArea("A1");
        Assertions.assertNull(area.getSheetId());
        Assertions.assertEquals(new Position(0, 0), area.getStartPosition());
        Assertions.assertNull(area.getEndPosition());

        Assertions.assertEquals(new Area(new SheetId(null, "sheet.0"), new Position(0, 0), new Position(9, 0)),
                translator.toArea("$'sheet.0'.A1:A10"));
        Assertions.assertEquals(new Area(new SheetId(null, "sheet.0"), new Position(0, 0), null),
                translator.toArea("$'sheet.0'.A1"));
    }


    /**
     * Not work yet.
     */
//    @Test
    public void testToExpression() {
        Translator translator = new Translator();
        Assertions.assertEquals("A1", translator.toExpression(new Position(0, 0)));
        Assertions.assertEquals("B1", translator.toExpression(new Position(0, 1)));
        Assertions.assertEquals("Z1", translator.toExpression(new Position(0, 25)));
        Assertions.assertEquals("AA1", translator.toExpression(new Position(0, 26)));
        Assertions.assertEquals("AB1", translator.toExpression(new Position(0, 27)));
        Assertions.assertEquals("BA1", translator.toExpression(new Position(0, 52)));
        Assertions.assertEquals("CA1", translator.toExpression(new Position(0, 78)));
        Assertions.assertEquals("YZ1", translator.toExpression(new Position(0, 675)));
        Assertions.assertEquals("ZA1", translator.toExpression(new Position(0, 676)));
        Assertions.assertEquals("ZZ1", translator.toExpression(new Position(0, 701)));
        Assertions.assertEquals("AAA1", translator.toExpression(new Position(0, 702)));
    }

}