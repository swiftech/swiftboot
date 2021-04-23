package org.swiftboot.sheet.util;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.swiftboot.sheet.BaseTest;
import org.swiftboot.sheet.constant.SheetFileType;
import org.swiftboot.sheet.meta.Picture;
import org.swiftboot.sheet.meta.Position;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Test {@link PoiUtils}
 *
 * @author allen
 * @see PoiUtils
 */
public class PoiUtilsTest extends BaseTest {

    @Test
    public void testGetOrCreateSheet() throws IOException {
        Workbook workbook = PoiUtils.initWorkbook(null, SheetFileType.TYPE_XLSX);
        Sheet s1 = PoiUtils.getOrCreateSheet(workbook, "sheet0");
        Assertions.assertEquals(1, workbook.getNumberOfSheets());
        Sheet s11 = PoiUtils.getOrCreateSheet(workbook, "sheet0");
        Assertions.assertEquals(1, workbook.getNumberOfSheets());

    }

    @Test
    public void testWritePicture() throws IOException {
        Picture pictureValue = super.pictureLoader.get();

//        InputStream templateIns = super.loadTemplate(SheetFileType.TYPE_XLSX);
//        Workbook workbook = PoiUtils.initWorkbook(templateIns, SheetFileType.TYPE_XLSX);
//        Sheet sheet = workbook.getSheetAt(0);
//        PoiUtils.writePicture(sheet, new Position(0, 0), null, pictureValue);

        Workbook workbook = PoiUtils.initWorkbook(null, SheetFileType.TYPE_XLSX);

        Sheet sAnchorToSingleCell = workbook.createSheet("anchor to single cell");
        PoiUtils.writePicture(sAnchorToSingleCell, new Position(1,1), new Position(1,1), pictureValue);

        Sheet sAnchorToMatrix = workbook.createSheet("anchor to matrix");
        PoiUtils.writePicture(sAnchorToMatrix, new Position(1,1), new Position(10, 5), pictureValue);

        Sheet sAnchorToLine = workbook.createSheet("anchor to line");
        PoiUtils.writePicture(sAnchorToLine, new Position(1,1), new Position(1, 5), pictureValue);

        Sheet sAnchorToLeftTopCel = workbook.createSheet("anchor to left top cell");
        PoiUtils.writePicture(sAnchorToLeftTopCel, new Position(1,1), null, pictureValue);
        PoiUtils.writePicture(sAnchorToLeftTopCel, new Position(1, 8), new Position(1, null), pictureValue);
        PoiUtils.writePicture(sAnchorToLeftTopCel, new Position(1, 16), new Position(null, 1), pictureValue);

        try (OutputStream outputStream = super.createOutputStream(false, SheetFileType.TYPE_XLSX)) {
            workbook.write(outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
