package org.swiftboot.sheet.exp;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.swiftboot.sheet.BaseTest;
import org.swiftboot.sheet.TestUtils;
import org.swiftboot.sheet.constant.SheetFileType;
import org.swiftboot.sheet.meta.SheetMetaBuilder;

import java.util.Arrays;
import java.util.List;

/**
 * @author swiftech
 */
public class BaseExporterTest extends BaseTest {

    final List<List<Object>> m1 = Arrays.asList(
            Arrays.asList(303, 304, 305, 999),
            Arrays.asList(403, 404, 405, 406),
            Arrays.asList(503, 504, 505, 506),
            Arrays.asList(603, 604, 605, 606)
    );

    final List<List<Object>> m2 = Arrays.asList(
            Arrays.asList(3030, 3040, 3050, 3060, 3070),
            Arrays.asList(4030, 4040, 4050, 4060, 4070),
            Arrays.asList(5030, 5040, 5050, 5060, 5070),
            Arrays.asList(6030, 6040, 6050, 6060, 6070),
            Arrays.asList(7030, 7040, 7050, 7060, 7070)
    );

    /**
     * Init export entity for testing.
     *
     * @return
     */
    protected ExportEntity initExportEntity() {
        ExportEntity exportEntity = new ExportEntity();
        exportEntity.setValue1("test a1");
        exportEntity.setValue2("test b1");
        exportEntity.setMatrix(m1);
        exportEntity.setLine(Arrays.asList("line[0]", "line[1]"));
        exportEntity.setPictureToExport(super.pictureLoader);
        exportEntity.setMatrix2(m2);
        exportEntity.setPictureToExport2(super.pictureLoader);
        return exportEntity;
    }

    /**
     * Init meta for testing export.
     *
     * @return
     */
    protected SheetMetaBuilder createSheetMetaBuilder(String sheetType) {
        SheetMetaBuilder builder = new SheetMetaBuilder();
        List<Integer> vertical = Arrays.asList(302, 402, 502, 999);
        List<Integer> vertical2 = Arrays.asList(1, 1, 1, 0);
        List<Integer> horizontal = Arrays.asList(203, 204, 205, 999);
        List<Integer> horizontal2 = Arrays.asList(1, 1, 1, 0);
        // Matrix
        List<List<Integer>> matrix4x4 = Arrays.asList(
                Arrays.asList(303, 304, 305, 306),
                Arrays.asList(403, 404, 405, 406),
                Arrays.asList(503, 504, 505, 506),
                Arrays.asList(603, 604, 605, 606)
        );

        // table titles
        builder.sheet(0, "my first sheet");

        builder.items(builder.itemBuilder()
                .newItem().key("column title").parse("B1:Z1").value(Arrays.asList(TestUtils.createLetters(12)))
                .newItem().key("row title").parse("A2:A26").value(Arrays.asList(TestUtils.createNumbers(12))))

                // Single
                .items(builder.itemBuilder()
                        .newItem().key("key-C2").parse("C2").value("vertical line 1")
                        .newItem().key("key-H2").parse("H2").value("vertical line 2")
                        .newItem().key("key-b3").parse("b3").value("horizontal line 1")
                        .newItem().key("key-b8").parse("b8").value("horizontal line 2"))

                // vertical line
                .items(builder.itemBuilder()
                        .newItem().key("key-c4:c6").parse("c4:c6").value(vertical) // vertical line, 999 shouldn't be exported
                        .newItem().key("key-h4:h?").parse("h4:h?").value(vertical2)) // vertical line with uncertain size, all exported

                // horizontal line
                .items(builder.itemBuilder()
                        .newItem().key("key-d3:f3").parse("d3:f3").value(horizontal) // horizontal line, 999 shouldn't be exported
                        .newItem().key("key-d8:?8").parse("d8:?8").value(horizontal2)) // horizontal line with uncertain size, all exported

                // matrix
                .items(builder.itemBuilder()
                        .newItem().key("key-d4:f6").parse("d4:f6").value(matrix4x4)
                        .newItem().key("key-c14:?").parse("c14:?").value(matrix4x4));

        if (!SheetFileType.TYPE_CSV.equals(sheetType)) {
            // merged matrix
            builder.items(builder.itemBuilder()
                    .newItem().key("merged-h14:k17").parse("h14:k17").merge().value(matrix4x4));
        }
        return builder;
    }

    @Test
    public void testAsMatrix() {
        System.out.println(StringUtils.join(
                BaseExporter.asMatrix("hello", 1, 1)));

        System.out.println("Horizontal: " + StringUtils.join(
                BaseExporter.asMatrix(Arrays.asList("hello", "world"), 1, 2)));

        System.out.println("Vertical: " + StringUtils.join(
                BaseExporter.asMatrix(Arrays.asList("hello", "world"), 2, 1)));

        System.out.println(StringUtils.join(
                BaseExporter.asMatrix(Arrays.asList(Arrays.asList("hello", "world"), Arrays.asList("goodbye", "love")), 2, 2)));
    }

}
