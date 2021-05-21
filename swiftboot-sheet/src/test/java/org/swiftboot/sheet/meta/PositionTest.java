package org.swiftboot.sheet.meta;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author allen
 * @see Position
 */
class PositionTest {

    @Test
    void enlarge() {
        Assertions.assertNull(Position.enlarge(null, null));
        Assertions.assertEquals(new Position(0, 0), Position.enlarge(new Position(0, 0), null));
        Assertions.assertEquals(new Position(0, 0), Position.enlarge(null, new Position(0, 0)));
        Assertions.assertEquals(new Position(2, 2), Position.enlarge(new Position(0, 2), new Position(2, 0)));
    }

    @Test
    void narrow() {
        Assertions.assertNull(Position.enlarge(null, null));
        Assertions.assertEquals(new Position(0, 0), Position.narrow(new Position(0, 0), null));
        Assertions.assertEquals(new Position(0, 0), Position.narrow(null, new Position(0, 0)));
        Assertions.assertEquals(new Position(0, 0), Position.narrow(new Position(0, 2), new Position(2, 0)));
    }
}