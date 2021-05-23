package org.swiftboot.sheet.meta;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author swiftech
 * @see SheetMeta
 */
class SheetMetaTest {

    @Test
    void findMaxPositionSimple() {
        SheetMetaBuilder builder = new SheetMetaBuilder();
        SheetMeta meta = builder.items(builder.itemBuilder()
                .newItem().key("1").parse("b2")).build();
        Assertions.assertEquals(new Position(1, 1), meta.findMaxPosition());
        Assertions.assertEquals(new Position(1, 1), meta.findMaxPosition(SheetId.DEFAULT_SHEET));
    }

    @Test
    void findMaxPosition() {
        SheetMetaBuilder builder = new SheetMetaBuilder();
        SheetMeta meta = builder
                .items(builder.itemBuilder()
                        .newItem().key("1").parse("B1:D3")
                        .newItem().key("2").parse("A2:C4")
                        .newItem().key("3").parse("c2:c3")).build();
//        SheetMeta meta = new SheetMeta();
//        meta.fromExpression("1", "B1:D3");
//        meta.fromExpression("2", "A2:C4");
//        meta.fromExpression("3", "c2:c3");
        Assertions.assertEquals(new Position(3, 3), meta.findMaxPosition());
    }
}