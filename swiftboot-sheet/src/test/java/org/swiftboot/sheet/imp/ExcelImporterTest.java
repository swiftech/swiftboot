package org.swiftboot.sheet.imp;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.swiftboot.sheet.constant.SheetFileType;
import org.swiftboot.sheet.excel.ExcelCellInfo;
import org.swiftboot.sheet.meta.SheetMeta;
import org.swiftboot.sheet.meta.SheetMetaBuilder;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

/**
 * @author swiftech
 */
class ExcelImporterTest extends BaseImporterTest {

    @Test
    void testImportExcel() throws IOException {
        testImportToMap(SheetFileType.TYPE_XLS);
        System.out.println();
        testImportToMap(SheetFileType.TYPE_XLSX);
        System.out.println();
        testImportToObject(SheetFileType.TYPE_XLS);
        System.out.println();
        testImportToObject(SheetFileType.TYPE_XLSX);
    }

    private void testImportToMap(String fileType) {
        Importer importer = factory.createImporter(fileType);

        SheetMeta meta = super.initTestMeta();

        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        URL url = loader.getResource("imp/import." + fileType);
        Assertions.assertNotNull(url);
        System.out.println("import from file: " + url);

        try {
            Map<String, Object> result = importer.importFromStream(url.openStream(), meta);
            super.assertResults(result, fileType);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void testImportToObject(String fileType) throws IOException {
        Importer importer = factory.createImporter(fileType);

        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        URL url = loader.getResource("imp/import." + fileType);

        Object result = importer.importFromStream(url.openStream(), ImportEntity.class);
        System.out.println(result);
    }

    @Test
    public void testImportCustomized() throws IOException {
        String fileType = SheetFileType.TYPE_XLS;
        Importer importer = factory.createImporter(fileType);

        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        URL url = loader.getResource("imp/import." + fileType);

        SheetMetaBuilder builder = new SheetMetaBuilder();
        builder.items(builder.itemBuilder().newItem().parse("B2").onCell((ExcelCellInfo target) -> {
            CellStyle cellStyle = target.getCell().getCellStyle();
            System.out.println(cellStyle.getFillBackgroundColor());
            Assertions.assertEquals(IndexedColors.AUTOMATIC.index, cellStyle.getFillBackgroundColor());
        }));

        importer.importFromStream(url.openStream(), builder.build());
    }

}