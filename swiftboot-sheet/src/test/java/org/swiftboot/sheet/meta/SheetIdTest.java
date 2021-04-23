package org.swiftboot.sheet.meta;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author allen
 */
public class SheetIdTest {

    /**
     * Test the equation of SheetId by equals() and hashCode()
     */
    @Test
    public void test(){
        {
            SheetId sheetId00 = new SheetId(0);
            SheetId sheetId01 = new SheetId(0);
            Assertions.assertEquals(sheetId00, sheetId01);
        }
        {
            SheetId sheetId0 = new SheetId(0);
            SheetId sheetId00 = new SheetId(0, "sheet 00");
            SheetId sheetId01 = new SheetId(0, "sheet 01");
            Assertions.assertEquals(sheetId0, sheetId00);
            Assertions.assertEquals(sheetId00, sheetId01);
        }
        {
            SheetId sheetId00 = new SheetId(0, "sheet 00");
            SheetId sheetId01 = new SheetId(0, "sheet 01");
            Map<SheetId, Object> map = new HashMap<>();
            map.put(sheetId00, "00");
            map.put(sheetId01, "01");
            Assertions.assertNotNull(map.get(new SheetId(0)));
        }
    }
}
