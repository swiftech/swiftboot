package org.swiftboot.sheet.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author swiftech
 */
class LetterUtilsTest {

    @Test
    public void test() {
        Assertions.assertEquals(0, LetterUtils.letterToIndex('a'));
        Assertions.assertEquals(25, LetterUtils.letterToIndex('z'));
        System.out.println();
    }

}