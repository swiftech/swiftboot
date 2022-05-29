package org.swiftboot.sheet.imp;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.swiftboot.sheet.constant.SheetFileType;
import org.swiftboot.sheet.meta.SheetMeta;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

/**
 * @author swiftech
 */
class CsvImporterTest extends BaseImporterTest {

    @Test
    void importFromStream() {
        Importer importer = factory.createImporter(SheetFileType.TYPE_CSV);
        SheetMeta meta = super.initTestMeta();

        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        URL url = loader.getResource("imp/import.csv");
        Assertions.assertNotNull(url);

        try {
            Map<String, Object> result = importer.importFromStream(url.openStream(), meta);
            super.assertResults(result, SheetFileType.TYPE_CSV);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}