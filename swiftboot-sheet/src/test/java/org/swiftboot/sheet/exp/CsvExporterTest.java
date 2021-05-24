package org.swiftboot.sheet.exp;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.swiftboot.sheet.SwiftBootSheetFactory;
import org.swiftboot.sheet.constant.SheetFileType;
import org.swiftboot.sheet.meta.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author swiftech
 */
public class CsvExporterTest extends BaseExporterTest {

    @Test
    public void testExportFromMappedObject() {
        Exporter exporter = new SwiftBootSheetFactory().createExporter(SheetFileType.TYPE_CSV);
        ExportEntity exportEntity = super.initExportEntity();
        try (OutputStream ous = super.createOutputStream(false, SheetFileType.TYPE_CSV)) {
            exporter.export(exportEntity, ous);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Test export with or without template file.
     */
    @Test
    public void testExportWithOrWithoutTemplate() {
        long startTime = System.currentTimeMillis();
        // With out template
        doTestExportFromMap(null);
        System.out.println();

        // With template
        try (InputStream templateIns = super.loadTemplate(SheetFileType.TYPE_CSV)) {
            doTestExportFromMap(templateIns);
            System.out.println();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.printf("Done with %dms%n", System.currentTimeMillis() - startTime);

    }

    public void doTestExportFromMap(InputStream templateIns) {
        Exporter exporter = new SwiftBootSheetFactory().createExporter(SheetFileType.TYPE_CSV);
        SheetMetaBuilder sheetMetaBuilder = super.createSheetMetaBuilder();
        sheetMetaBuilder.items(sheetMetaBuilder.itemBuilder().newItem().key("picture").parse("A1").value(super.pictureLoader)); // This should be ignored because exporting picture is not supported.
        SheetMeta exportMeta = sheetMetaBuilder.build();
        printMeta(exportMeta);
        boolean isFromTemplate = templateIns != null;
        try (OutputStream fileOutputStream = super.createOutputStream(isFromTemplate, SheetFileType.TYPE_CSV)) {
            if (isFromTemplate) {
                exporter.export(templateIns, exportMeta, fileOutputStream);
            }
            else {
                exporter.export(exportMeta, fileOutputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testExtendSheet() {
        CsvExporter csvExporter = new CsvExporter(SheetFileType.TYPE_CSV);
        // Extend empty sheet
        List<String> lines = new ArrayList<>();
        csvExporter.extendSheet(lines, new Position(2, 2));
        System.out.println(StringUtils.join(lines, "\n"));
        System.out.println();
        // Extend existed sheet
        lines = new ArrayList<String>() {
            {
                add("a1,b1,");
                add(",b2,");
                add(",,c3");
                add("");
                add(",");
                add(",,,");
            }
        };
        csvExporter.extendSheet(lines, new Position(7, 6));
        System.out.println(StringUtils.join(lines, "\n"));
    }

    @Test
    public void testSetValueToRow() {
        CsvExporter csvExporter = new CsvExporter(SheetFileType.TYPE_CSV);
        List<String> rows = new ArrayList<String>() {
            {
                add(",,,,,,,");
            }
        };
        List<Object> values = new ArrayList<Object>() {
            {
                add(2);
                add(3);
            }
        };
        Position startPos = new Position(0, 1);
        // Normal
        csvExporter.setValuesToRow(rows, startPos, 2, values);
        System.out.println(StringUtils.join(rows));

        // Less values(should no exception)
        csvExporter.setValuesToRow(rows, startPos, 3, values);
        System.out.println(StringUtils.join(rows));

        // More values
        values.add(4);
        csvExporter.setValuesToRow(rows, startPos, 2, values);
        System.out.println(StringUtils.join(rows));
    }

    private void printMeta(SheetMeta meta) {
        meta.setAllowFreeSize(true);// TODO remove it!
        meta.accept(new MetaVisitor() {
            @Override
            public void visitMetaItem(MetaItem metaItem, Position startPos, Integer rowCount, Integer columnCount) {
                System.out.printf("%s: %s %d rows %d columns%n", metaItem.getKey(), startPos, rowCount, columnCount);
            }
        });
    }

    private void printCsv(String[] csvRows) {
        Arrays.stream(csvRows).forEach(new Consumer<String>() {
            @Override
            public void accept(String s) {

            }
        });
    }
}
