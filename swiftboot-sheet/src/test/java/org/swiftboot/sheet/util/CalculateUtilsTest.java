package org.swiftboot.sheet.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.swiftboot.sheet.util.CalculateUtils.powForExcel;

/**
 * @author allen
 */
class CalculateUtilsTest {

    @Test
    public void test() {

        Assertions.assertEquals(0, powForExcel(0));
        Assertions.assertEquals(26, powForExcel(1));
        Assertions.assertEquals(676, powForExcel(2));
        Assertions.assertEquals(17576, powForExcel(3));
        System.out.println();
    }

}