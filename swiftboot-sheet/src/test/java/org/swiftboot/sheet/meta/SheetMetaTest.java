package org.swiftboot.sheet.meta;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author allen
 */
class SheetMetaTest {

    @Test
    void findMaxPositionSimple() {
        SheetMeta meta = new SheetMeta();
        meta.fromExpression("1", "b2");
        Assertions.assertEquals(new Position(1, 1), meta.findMaxPosition());
    }

    @Test
    void findMaxPosition() {
        SheetMeta meta = new SheetMeta();
        meta.fromExpression("1", "B1:D3");
        meta.fromExpression("2", "A2:C4");
        meta.fromExpression("3", "c2:c3");
        Assertions.assertEquals(new Position(3, 3), meta.findMaxPosition());
    }
}