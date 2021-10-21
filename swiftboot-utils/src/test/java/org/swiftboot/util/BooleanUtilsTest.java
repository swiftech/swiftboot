package org.swiftboot.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static java.lang.Boolean.*;

/**
 * @author allen
 */
class BooleanUtilsTest {

    @Test
    void or() {
        Assertions.assertTrue(BooleanUtils.or(TRUE, FALSE));
        Assertions.assertFalse(BooleanUtils.or(FALSE, FALSE));
        Assertions.assertTrue(BooleanUtils.or(FALSE, TRUE, FALSE));
        Assertions.assertNull(BooleanUtils.or(new Boolean[]{null}));
        Assertions.assertNull(BooleanUtils.or(null, null));
        Assertions.assertTrue(BooleanUtils.or(FALSE, TRUE, FALSE, null));
        Assertions.assertFalse(BooleanUtils.or(FALSE, FALSE, null));

    }
}