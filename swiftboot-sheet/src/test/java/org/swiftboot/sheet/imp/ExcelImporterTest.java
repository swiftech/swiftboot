package org.swiftboot.sheet.imp;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.swiftboot.sheet.constant.SheetFileType;
import org.swiftboot.sheet.excel.ExcelCellInfo;
import org.swiftboot.sheet.meta.BaseCellInfo;
import org.swiftboot.sheet.meta.Picture;
import org.swiftboot.sheet.meta.SheetMeta;
import org.swiftboot.sheet.meta.SheetMetaBuilder;
import org.swiftboot.util.CryptoUtils;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

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
        builder.items(builder.itemBuilder().newItem().key("GET-B2").parse("B2").onCell((ExcelCellInfo target) -> {
            CellStyle cellStyle = target.getCell().getCellStyle();
            System.out.printf("backcolor: %s%n", cellStyle.getFillBackgroundColor());
            Assertions.assertEquals(IndexedColors.AUTOMATIC.index, cellStyle.getFillBackgroundColor());
        }));

        importer.importFromStream(url.openStream(), builder.build());
    }

    @Test
    public void testImportUncertainRows() throws IOException {
        String fileType = SheetFileType.TYPE_XLS;
        Importer importer = factory.createImporter(fileType);

        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        URL url = loader.getResource("imp/import." + fileType);

        SheetMetaBuilder builder = new SheetMetaBuilder();
        builder.items(builder.itemBuilder()
                .newItem().key("GET-B2-C3").parse("B2:C3")
                .newItem().key("GET-B2-5?").from("B2").to(null, 5)
                .onCell((ExcelCellInfo target) -> {
                    System.out.printf("found: [%d,%d]%n", target.getRowIdx(), target.getColIdx());
                }));

        Map<String, Object> result = importer.importFromStream(url.openStream(), builder.build());
        Assertions.assertEquals(2, result.size());
        displayData(result);
    }

    @Test
    public void testImportFromPredicatedPosition() throws IOException {
        String fileType = SheetFileType.TYPE_XLS;
        Importer importer = factory.createImporter(fileType);

        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        URL url = loader.getResource("imp/import." + fileType);

        SheetMetaBuilder builder = new SheetMetaBuilder();
        builder.items(builder.itemBuilder()
                .newItem().key("GET-C3").predict((Predicate<BaseCellInfo>) cellInfo ->
                        "c3".equals(cellInfo.getValue()), 2, 3)
                .onCell((ExcelCellInfo target) -> {
                    System.out.printf("found: [%d,%d]%n", target.getRowIdx(), target.getColIdx());
                }));

        Map<String, Object> result = importer.importFromStream(url.openStream(), builder.build());
        Assertions.assertEquals(1, result.size());
        displayData(result);
    }

    @Test
    public void testImportImage() throws IOException {
        String fileType = SheetFileType.TYPE_XLS;
        Importer importer = factory.createImporter(fileType);

        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        URL url = loader.getResource("imp/import." + fileType);

        SheetMetaBuilder builder = new SheetMetaBuilder();
        SheetMeta sheetMeta = builder
                .withImages()
                .imageConverter((Function<Picture, ?>) pic -> CryptoUtils.md5(pic.getData()))
                .items(builder.itemBuilder()
                .newItem().key("test-image").from("A6")
        ).build();
        Map<String, Object> result = importer.importFromStream(url.openStream(), sheetMeta);
        Assertions.assertEquals(1, result.size());
        displayData(result);
    }

    private void displayData(Map<String, Object> result) {
        for (String key : result.keySet()) {
            System.out.printf("Sheet: %s%n", key);
            Object item = result.get(key);
            if (item instanceof List) {
                List l = (List) item;
                System.out.printf("%s -> %s%n", key, StringUtils.join(l, ", "));
            }
            else if (item instanceof String) {
                String str = (String) item;
                System.out.printf("%s -> %s%n", key, str);
            }

        }
    }
}