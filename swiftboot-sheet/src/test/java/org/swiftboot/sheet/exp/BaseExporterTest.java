package org.swiftboot.sheet.exp;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.swiftboot.sheet.BaseTest;
import org.swiftboot.sheet.TestUtils;
import org.swiftboot.sheet.meta.SheetMeta;

import java.util.Arrays;
import java.util.List;

/**
 * @author allen
 */
public class BaseExporterTest extends BaseTest {

    /**
     * Init export entity for testing.
     *
     * @return
     */
    protected ExportEntity initExportEntity() {
        ExportEntity exportEntity = new ExportEntity();
        exportEntity.setValue1("test a1");
        exportEntity.setValue2("test b1");
        exportEntity.setMatrix(Arrays.asList(
                Arrays.asList(303, 304, 305, 999),
                Arrays.asList(403, 404, 405, 406),
                Arrays.asList(503, 504, 505, 506),
                Arrays.asList(603, 604, 605, 606)
        ));
        exportEntity.setLine(Arrays.asList("line[0]", "line[1]"));
        exportEntity.setPictureToExport(super.pictureLoader);
        return exportEntity;
    }

    /**
     * Init meta for testing export.
     *
     * @return
     */
    protected SheetMeta initTestMeta() {
        SheetMeta meta = new SheetMeta();

        // table titles
        meta.fromExpression("column title", "B1:Z1", Arrays.asList(TestUtils.createLetters(12)));
        meta.fromExpression("row title", "A2:A26", Arrays.asList(TestUtils.createNumbers(12)));

        // Single
        meta.fromExpression("key-C2", "C2", "vertical line 1");
        meta.fromExpression("key-H2", "H2", "vertical line 2");
        meta.fromExpression("key-b3", "b3", "horizontal line 1");
        meta.fromExpression("key-b8", "b8", "horizontal line 2");

        // vertical line
        List<Integer> vertical = Arrays.asList(302, 402, 502, 999);
        meta.fromExpression("key-c4:c6", "c4:c6", vertical); // vertical line, 999 shouldn't be exported
        List<Integer> vertical2 = Arrays.asList(1, 1, 1, 0);
        meta.fromExpression("key-h4:h?", "h4:h?", vertical2); // vertical line with uncertain size, all exported

        // horizontal line
        List<Integer> horizontal = Arrays.asList(203, 204, 205, 999);
        meta.fromExpression("key-d3:f3", "d3:f3", horizontal); // horizontal line, 999 shouldn't be exported
        List<Integer> horizontal2 = Arrays.asList(1, 1, 1, 0);
        meta.fromExpression("key-d8:?8", "d8:?8", horizontal2); // horizontal line with uncertain size, all exported

        // Matrix
        List<List<Integer>> matrix = Arrays.asList(
                Arrays.asList(303, 304, 305, 306),
                Arrays.asList(403, 404, 405, 406),
                Arrays.asList(503, 504, 505, 506),
                Arrays.asList(603, 604, 605, 606)
        );
        meta.fromExpression("key-d4:f6", "d4:f6", matrix);
        meta.fromExpression("key-c14:?", "c14:?", matrix);
        return meta;
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
