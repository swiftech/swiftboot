package org.swiftboot.sheet.imp;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.swiftboot.sheet.SwiftBootSheetFactory;
import org.swiftboot.sheet.constant.SheetFileType;
import org.swiftboot.sheet.meta.MetaItem;
import org.swiftboot.sheet.meta.Position;
import org.swiftboot.sheet.meta.SheetMeta;
import org.swiftboot.sheet.meta.Translator;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
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
        Map<MetaItem, Object> metas = new HashMap<>();
        SheetMeta meta = new SheetMeta(metas);
        Translator translator = new Translator();
        // Single cells
        metas.put(new MetaItem("key-b1", translator.toArea("B1")), null);
        metas.put(new MetaItem("key-a2", translator.toArea("A2")), null);
        metas.put(new MetaItem("key-x1", translator.toArea("X1")), null);
        meta.addItem("key-b1-bypos", new Position(0, 1), null);
        // Merged cell
        metas.put(new MetaItem("key-5ad: a5", translator.toArea("A5")), null);
        metas.put(new MetaItem("key-5ad: b5", translator.toArea("B5")), null);
        metas.put(new MetaItem("key-5ad: c5", translator.toArea("C5")), null);
        metas.put(new MetaItem("key-5ad: d5", translator.toArea("D5")), null);

        // line-cells
        metas.put(new MetaItem("key-b2:d2", translator.toArea("B2:D2")), null);
        metas.put(new MetaItem("key-b2:b4", translator.toArea("B2:B4")), null);

        // matrix cells
        metas.put(new MetaItem("key-b2:c3", translator.toArea("b2:c3")), null);

        // Empty
        meta.fromExpression("key-x1", "x1");
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
