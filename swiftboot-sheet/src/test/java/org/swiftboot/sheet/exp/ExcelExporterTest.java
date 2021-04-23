package org.swiftboot.sheet.exp;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.jupiter.api.Test;
import org.swiftboot.sheet.SwiftBootSheetFactory;
import org.swiftboot.sheet.constant.SheetFileType;
import org.swiftboot.sheet.meta.Position;
import org.swiftboot.sheet.meta.SheetMeta;
import org.swiftboot.sheet.meta.SheetMetaBuilder;
import org.swiftboot.sheet.util.PoiUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author allen
 */
public class ExcelExporterTest extends BaseExporterTest {

    @Test
    public void testExportFromMappedObject() {
        Exporter exporter = new SwiftBootSheetFactory().createExporter(SheetFileType.TYPE_XLS);
        ExportEntity exportEntity = super.initExportEntity();
        try (OutputStream ous = super.createOutputStream(false, SheetFileType.TYPE_XLS)) {
            exporter.export(exportEntity, ous);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
                .newItem().key("matrix").parse("B2:C5").value(pictureLoader)
                .newItem().key("horizontal").parse("B3:C3").value(pictureLoader)
                .newItem().key("vertical").parse("D2:D5").value(pictureLoader)
                .newItem().key("single cell by position").from(10, 10).value(pictureLoader)).build();

        try (OutputStream outputStream = super.createOutputStream(false, SheetFileType.TYPE_XLSX)) {
            xlsxExporter.export(meta, outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
