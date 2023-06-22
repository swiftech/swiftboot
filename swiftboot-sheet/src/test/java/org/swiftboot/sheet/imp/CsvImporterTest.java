package org.swiftboot.sheet.imp;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.swiftboot.sheet.CsvCellInfo;
import org.swiftboot.sheet.constant.SheetFileType;
import org.swiftboot.sheet.meta.*;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

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

    @Test
    void importFromStream2() throws IOException {
        Importer importer = factory.createImporter(SheetFileType.TYPE_CSV);
        SheetMetaBuilder builder = new SheetMetaBuilder();
        builder.items(builder.itemBuilder()
                .newItem().key("GET-B2").predict((Predicate<BaseCellInfo>) cellInfo -> {
                    return "c3".equals(cellInfo.getValue().toString().trim());
                }, 2, 2).onCell((Consumer<CsvCellInfo>) cellInfo -> {
                    System.out.println("# " + cellInfo.getValue());
                })
        );
        SheetMeta meta = builder.build();

        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        URL url = loader.getResource("imp/import.csv");
        Assertions.assertNotNull(url);

        Map<String, Object> result = importer.importFromStream(url.openStream(), meta);
        Assertions.assertEquals(1, result.size());
        for (String key : result.keySet()) {
            List one = (List) result.get(key);
            System.out.printf("%s -> %s%n", key, StringUtils.join(one, ", "));
        }

    }
}