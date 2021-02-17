package org.swiftboot.sheet.exp;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.jupiter.api.Test;
import org.swiftboot.sheet.SwiftBootSheetFactory;
import org.swiftboot.sheet.constant.SheetFileType;
import org.swiftboot.sheet.meta.Position;
import org.swiftboot.sheet.meta.SheetMeta;
import org.swiftboot.sheet.util.PoiUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 *
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

    private void doExportExcel(InputStream inputStream, String fileType) {
        Exporter exporter = new SwiftBootSheetFactory().createExporter(fileType);
        SheetMeta exportMeta = super.initTestMeta();
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
        SheetMeta meta = new SheetMeta();

        meta.fromExpression("single cell", "B2", pictureLoader);

        meta.fromExpression("matrix", "B2:C5", pictureLoader);

        meta.fromExpression("horizontal", "B3:C3", pictureLoader);

        meta.fromExpression("vertical", "D2:D5", pictureLoader);

        try (OutputStream outputStream = super.createOutputStream(false, SheetFileType.TYPE_XLSX)) {
            xlsxExporter.export(meta, outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
