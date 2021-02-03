package org.swiftboot.sheet.meta;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author allen
 */
class AreaTest {

    @Test
    void size() {
        Area area = new Area(new Position(1, 1));
        Assertions.assertEquals(1, area.size());
        area.setEndPosition(new Position(1, 1));
        Assertions.assertEquals(1, area.size());
        area.setEndPosition(new Position(2, 2));
        Assertions.assertEquals(4, area.size());
        area.setEndPosition(new Position(5, 10));
        Assertions.assertEquals(50, area.size());
    }

    @Test
    void isSingleCell() {
        Area area = new Area(new Position(1, 1));
        Assertions.assertTrue(area.isSingleCell());
        area.setEndPosition(new Position(1, 1));
        Assertions.assertTrue(area.isSingleCell());
        area.setEndPosition(new Position(2, 2));
        Assertions.assertFalse(area.isSingleCell());
    }
}