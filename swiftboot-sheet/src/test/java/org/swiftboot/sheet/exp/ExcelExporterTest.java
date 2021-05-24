package org.swiftboot.sheet.exp;

import org.apache.poi.ss.usermodel.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.swiftboot.sheet.SwiftBootSheetFactory;
import org.swiftboot.sheet.constant.SheetFileType;
import org.swiftboot.sheet.excel.ExcelCellInfo;
import org.swiftboot.sheet.meta.Position;
import org.swiftboot.sheet.meta.SheetMeta;
import org.swiftboot.sheet.meta.SheetMetaBuilder;
import org.swiftboot.sheet.util.PoiUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

/**
 * @author swiftech
 */
public class ExcelExporterTest extends BaseExporterTest {

    /**
     * Test export from an annotated class object without template.
     */
    @Test
    public void testExportFromMappedObject() {
        Exporter exporter = new SwiftBootSheetFactory().createExporter(SheetFileType.TYPE_XLS);
        ExportEntity exportEntity = super.initExportEntity();
        try (OutputStream ous = super.createOutputStream(false, SheetFileType.TYPE_XLS, "annotated")) {
            exporter.export(exportEntity, ous);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Test export to xls and xlsx file with and without template,
     */
    @Test
    public void testExportExcel() {
        long startTime = System.currentTimeMillis();
        doExportWithAndWithoutTemplate(SheetFileType.TYPE_XLS);
        doExportWithAndWithoutTemplate(SheetFileType.TYPE_XLSX);
        System.out.printf("Done with %dms%n", System.currentTimeMillis() - startTime);
    }

    private void doExportWithAndWithoutTemplate(String fileType) {
        // Export xls without template
        doExportExcel(null, fileType);

        try (InputStream insXls = loadTemplate(fileType)) {
            // Export xls with template
            doExportExcel(insXls, fileType);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Do export to a excel whose type is specified by fileType, if inputStream is null,
     * export it without template.
     *
     * @param inputStream input stream of template excel file.
     * @param fileType
     */
    private void doExportExcel(InputStream inputStream, String fileType) {
        Exporter exporter = new SwiftBootSheetFactory().createExporter(fileType);
        SheetMeta exportMeta = super.createSheetMetaBuilder().build();
        boolean isFromTemplate = inputStream != null;
        try (OutputStream fileOutputStream = super.createOutputStream(isFromTemplate, fileType)) {
            if (isFromTemplate) {
                exporter.export(inputStream, exportMeta, fileOutputStream);
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
        // Without template(xls)
        ExcelExporter xlsExporter = new ExcelExporter(SheetFileType.TYPE_XLS);
        try {
            Workbook wb = PoiUtils.initWorkbook(null, SheetFileType.TYPE_XLSX);
            Sheet sheet = PoiUtils.firstSheet(wb);
            xlsExporter.extendSheet(sheet, new Position(0, 0));
            System.out.println(sheet.getPhysicalNumberOfRows());
            xlsExporter.extendSheet(sheet, new Position(99, 99));
            System.out.println(sheet.getPhysicalNumberOfRows());
        } catch (IOException e) {
            e.printStackTrace();
        }


        // With template(xlsx)
        System.out.println();
        ExcelExporter xlsxExporter = new ExcelExporter(SheetFileType.TYPE_XLSX);
        try (InputStream insXlsx = loadTemplate(SheetFileType.TYPE_XLSX)) {
            Workbook wb = PoiUtils.initWorkbook(insXlsx, SheetFileType.TYPE_XLSX);
            Sheet sheet = PoiUtils.firstSheet(wb);
            xlsxExporter.extendSheet(sheet, new Position(0, 0));
            System.out.println(sheet.getPhysicalNumberOfRows());
            xlsxExporter.extendSheet(sheet, new Position(99, 99));
            System.out.println(sheet.getPhysicalNumberOfRows());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testExportPicture() {
        ExcelExporter xlsxExporter = new ExcelExporter(SheetFileType.TYPE_XLSX);

        SheetMetaBuilder builder = new SheetMetaBuilder();
        SheetMeta meta;
        meta = builder.items(builder.itemBuilder()
                .newItem().key("single cell").parse("B2").value(pictureLoader)
                .newItem().key("matrix").parse("B2:C5").merge().value(pictureLoader)
                .newItem().key("horizontal").parse("B3:C3").value(pictureLoader)
                .newItem().key("vertical").parse("D2:D5").value(pictureLoader)
                .newItem().key("single cell by position").from(10, 10).value(pictureLoader)).build();

        Assertions.assertDoesNotThrow(() -> {
            try (OutputStream outputStream = super.createOutputStream(false, SheetFileType.TYPE_XLSX, "picutures")) {
                xlsxExporter.export(meta, outputStream);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        });
    }

    @Test
    public void testExportCustomized() {
        ExcelExporter xlsxExporter = new ExcelExporter(SheetFileType.TYPE_XLSX);
        SheetMetaBuilder builder = new SheetMetaBuilder();
        List<List<Integer>> matrix = Arrays.asList(
                Arrays.asList(303, 304, 305, 306),
                Arrays.asList(403, 404, 405, 406),
                Arrays.asList(503, 504, 505, 506),
                Arrays.asList(603, 604, 605, 606)
        );
        builder.items(builder.itemBuilder()
                .newItem().key("customized cell").parse("B2:F5").value(matrix).onCell((ExcelCellInfo info) -> {
                    System.out.printf("%s - %s - %s (%s.%s)%n", info.getWorkbook(), info.getSheet(), info.getCell(), info.getRowIdx(), info.getColIdx());
                    CellStyle cellStyle = info.getWorkbook().createCellStyle();
                    if ((info.getRowIdx() + info.getColIdx()) % 2 == 0) {
                        cellStyle.setFillBackgroundColor(IndexedColors.AQUA.getIndex());
                        cellStyle.setFillPattern(FillPatternType.BIG_SPOTS);
                        info.getCell().setCellStyle(cellStyle);
                    }
                }));
        Assertions.assertDoesNotThrow(() -> {
            try (OutputStream outputStream = createOutputStream(false, SheetFileType.TYPE_XLSX, "customized")) {
                xlsxExporter.export(builder.build(), outputStream);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        });

    }

}
