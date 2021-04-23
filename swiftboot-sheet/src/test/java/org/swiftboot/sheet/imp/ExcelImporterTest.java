package org.swiftboot.sheet.imp;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.swiftboot.sheet.constant.SheetFileType;
import org.swiftboot.sheet.meta.SheetMeta;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

/**
 * @author allen
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
            super.assertResults(result);
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

}