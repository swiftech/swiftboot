package org.swiftboot.sheet.meta;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.swiftboot.sheet.imp.ImportEntity;

import java.util.List;

/**
 * @author swiftech
 * @see SheetMetaBuilder
 */
public class SheetMetaBuilderTest {

    @Test
    public void testDefaultSheet() {
        SheetMetaBuilder builder = new SheetMetaBuilder();
        builder.items(builder.itemBuilder().newItem().key("s1k1").from(0, 0).to(9, 9).value("s1v1"));
        SheetMeta meta = builder.build();
        List<MetaItem> metaItems = meta.getMetaItems(0);
        Assertions.assertFalse(metaItems.isEmpty());
        Assertions.assertEquals("s1k1", metaItems.get(0).getKey());
        Assertions.assertEquals("s1v1", metaItems.get(0).getValue());
        Assertions.assertEquals(new Position(0, 0), metaItems.get(0).getArea().startPosition);
        Assertions.assertEquals(new Position(9, 9), metaItems.get(0).getArea().endPosition);
    }

//    @Test
//    public void t() {
//        SheetMetaBuilder builder = new SheetMetaBuilder();
//        builder.items(builder.itemBuilder().key("").newItem());
//    }

    @Test
    public void test() {
        SheetMetaBuilder builder = new SheetMetaBuilder();
        builder.items(builder.itemBuilder()
                .newItem().key("s0k1").from(0, 0).to(9, 9).value("s1v1"))
                .sheet("sheet x")
                .items(builder.itemBuilder()
                        .newItem().key("s1k1").from(0, 0).to(9, 9).value("s1v1")
                        .newItem().key("s1k2").from(10, 10).value("s1v2")
                )
                .sheet(1, "sheet 2")
                .items(builder.itemBuilder()
                        .newItem().key("s2k1").from(100, 100).value("s2v1")
                        .newItem().key("s2k2").parse("X24")
                )
                .fromAnnotatedObject(new ImportEntity());

        SheetMeta sheetMeta = builder.build();
        for (MetaItem metaItem : sheetMeta.getAllMetaItems()) {
            System.out.println(metaItem);
        }

        List<MetaItem> items = sheetMeta.getMetaItems(0);
        Assertions.assertNotNull(items);
        Assertions.assertEquals(1, items.size());

        items = sheetMeta.getMetaItems("sheet x");
        Assertions.assertNotNull(items);
        Assertions.assertEquals(2, items.size());

        items = sheetMeta.getMetaItems(1);
        Assertions.assertNotNull(items);
        Assertions.assertEquals(6, items.size());
    }

    @Test
    public void predicate() {

        // can't use numbers and function for method from() at the same time
        Assertions.assertThrows(Exception.class, () -> {
            SheetMetaBuilder builder = new SheetMetaBuilder();
            builder.items(builder.itemBuilder()
                    .key("test").from(0, 0).from(cellInfo -> false, 1, 1));
        });

        // can't use numbers and function for method from() at the same time
        Assertions.assertThrows(Exception.class, () -> {
            SheetMetaBuilder builder = new SheetMetaBuilder();
            builder.items(builder.itemBuilder()
                    .key("test").from("B2").from(cellInfo -> false, 1, 1));
        });

        // can't use numbers and function for method from() at the same time
        Assertions.assertThrows(Exception.class, () -> {
            SheetMetaBuilder builder = new SheetMetaBuilder();
            builder.items(builder.itemBuilder()
                    .key("test").from(cellInfo -> false, 1, 1).from(0, 0));
        });

        // muse specify rows for predicate
        Assertions.assertThrows(Exception.class, () -> {
            SheetMetaBuilder builder = new SheetMetaBuilder();
            builder.items(builder.itemBuilder()
                    .key("test").from(cellInfo -> false, null, 1));
        });

        // muse specify columns for predicate
        Assertions.assertThrows(Exception.class, () -> {
            SheetMetaBuilder builder = new SheetMetaBuilder();
            builder.items(builder.itemBuilder()
                    .key("test").from(cellInfo -> false, 1, null));
        });


    }
}
