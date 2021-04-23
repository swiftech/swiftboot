package org.swiftboot.sheet.imp;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.swiftboot.sheet.SwiftBootSheetFactory;
import org.swiftboot.sheet.meta.SheetMeta;
import org.swiftboot.sheet.meta.SheetMetaBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author allen
 */
public class BaseImporterTest {
    protected SwiftBootSheetFactory factory = new SwiftBootSheetFactory();

    protected final List<String> horizontalLine = Arrays.asList(new String[]{"b2", "c2", "d2"}.clone());
    protected final List<String> verticalLine = Arrays.asList(new String[]{"b2", "b3", "b4"}.clone());
    protected final List<List<String>> matrix = Arrays.asList(
            Arrays.asList(new String[]{"b2", "c2"}.clone()),
            Arrays.asList(new String[]{"b3", "c3"}.clone())
    );

    protected SheetMeta initTestMeta() {
        SheetMetaBuilder builder = new SheetMetaBuilder();
        // Single cells
        SheetMeta meta = builder.items(builder.itemBuilder()
                .newItem().key("key-b1").parse("B1")
                .newItem().key("key-a2").parse("A2")
                .newItem().key("key-x1").parse("X1")
                .newItem().key("key-b1-bypos").from(0, 1))
                // Merged cell
                .items(builder.itemBuilder()
                        .newItem().key("key-5ad: a5").parse("A5")
                        .newItem().key("key-5ad: b5").parse("B5")
                        .newItem().key("key-5ad: c5").parse("C5")
                        .newItem().key("key-5ad: d5").parse("D5"))

                // line-cells
                .items(builder.itemBuilder()
                        .newItem().key("key-b2:d2").parse("B2:D2")
                        .newItem().key("key-b2:b4").parse("B2:B4"))

                // matrix cells
                .items(builder.itemBuilder().newItem().key("key-b2:c3").parse("b2:c3"))

                // Empty
                .items(builder.itemBuilder().newItem().key("key-x1").parse("x1")).build();
        return meta;
    }

    protected void assertResults(Map<String, Object> result) {
        for (String key : result.keySet()) {
            Object v = result.get(key);
            if (v == null) {
                System.out.printf("%s = null%n", key);
            }
            else {
                System.out.printf("%s = '%s'%n", key, result.get(key));
            }
        }
        Assertions.assertEquals("b1", result.get("key-b1"));
        Assertions.assertEquals("a2", result.get("key-a2"));
        Assertions.assertNull(result.get("key-x1"));
        Assertions.assertEquals("b1", result.get("key-b1-bypos"));
        Assertions.assertEquals(horizontalLine, result.get("key-b2:d2"));
        Assertions.assertEquals(verticalLine, result.get("key-b2:b4"));
        Assertions.assertEquals(matrix, result.get("key-b2:c3"));

        //Empty
        Assertions.assertNull(result.get("key-x1"));
    }


    @Test
    public void testShrinkMatrix() {
        List<List<Object>> matrix = Arrays.asList(Arrays.asList("hello", "world"), Arrays.asList("goodbye", "love"));
        System.out.println(BaseImporter.shrinkMatrix(matrix, 1, 1));
        System.out.println(BaseImporter.shrinkMatrix(matrix, 1, null));
        System.out.println(BaseImporter.shrinkMatrix(matrix, null, 1));
        System.out.println(BaseImporter.shrinkMatrix(matrix, 2, 2));
    }
}
