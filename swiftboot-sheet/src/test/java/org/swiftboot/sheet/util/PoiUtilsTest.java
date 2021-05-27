package org.swiftboot.sheet.util;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.swiftboot.sheet.BaseTest;
import org.swiftboot.sheet.constant.SheetFileType;
import org.swiftboot.sheet.meta.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Test {@link PoiUtils}
 *
 * @author swiftech
 * @see PoiUtils
 */
public class PoiUtilsTest extends BaseTest {

    public static final String SHEET_NAME_FOR_UNIT_TEST1 = "sheet name for unit test 1";
    public static final String SHEET_NAME_FOR_UNIT_TEST2 = "sheet name for unit test 2";
    public static final String SHEET_NAME_FOR_UNIT_TEST3 = "sheet name for unit test 3";

    @Test
    public void testGetOrCreateSheetBySheetId() throws IOException {
        Workbook workbook = PoiUtils.initWorkbook(null, SheetFileType.TYPE_XLS);
        SheetId sheetId = new SheetId();

        // invalid sheet id should throws exception
        Assertions.assertThrows(Exception.class, () -> PoiUtils.getOrCreateSheet(workbook, sheetId));

        // Only provides index of sheet
        sheetId.setSheetIndex(0);
        Sheet sheet = PoiUtils.getOrCreateSheet(workbook, sheetId);
        Assertions.assertNotNull(sheet);
        Assertions.assertEquals(SheetId.DEFAULT_SHEET_NAME, sheet.getSheetName());
        Assertions.assertEquals(0, workbook.getSheetIndex(sheet));

        // Overwrite sheet name
        sheetId.setSheetName(SHEET_NAME_FOR_UNIT_TEST1);
        sheet = PoiUtils.getOrCreateSheet(workbook, sheetId);
        Assertions.assertNotNull(sheet);
        Assertions.assertEquals(SHEET_NAME_FOR_UNIT_TEST1, sheet.getSheetName());
        Assertions.assertEquals(0, workbook.getSheetIndex(sheet));

        // create sheet 2 automatically
        sheetId.setSheetIndex(1);
        sheetId.setSheetName(SHEET_NAME_FOR_UNIT_TEST2);
        sheet = PoiUtils.getOrCreateSheet(workbook, sheetId);
        Assertions.assertNotNull(sheet);
        Assertions.assertEquals(SHEET_NAME_FOR_UNIT_TEST2, sheet.getSheetName());
        Assertions.assertEquals(1, workbook.getSheetIndex(sheet));

        // change first sheet name
        sheetId.setSheetIndex(0);
        sheetId.setSheetName(SHEET_NAME_FOR_UNIT_TEST3);
        sheet = PoiUtils.getOrCreateSheet(workbook, sheetId);
        Assertions.assertNotNull(sheet);
        Assertions.assertEquals(SHEET_NAME_FOR_UNIT_TEST3, sheet.getSheetName());
        Assertions.assertEquals(0, workbook.getSheetIndex(sheet));


    }

    @Test
    public void testGetOrCreateSheetByName() throws IOException {
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
        PoiUtils.writePicture(sAnchorToSingleCell, new Position(1, 1), new Position(1, 1), pictureValue);

        Sheet sAnchorToMatrix = workbook.createSheet("anchor to matrix");
        PoiUtils.writePicture(sAnchorToMatrix, new Position(1, 1), new Position(10, 5), pictureValue);

        Sheet sAnchorToLine = workbook.createSheet("anchor to line");
        PoiUtils.writePicture(sAnchorToLine, new Position(1, 1), new Position(1, 5), pictureValue);

        Sheet sAnchorToLeftTopCel = workbook.createSheet("anchor to left top cell");
        PoiUtils.writePicture(sAnchorToLeftTopCel, new Position(1, 1), null, pictureValue);
        PoiUtils.writePicture(sAnchorToLeftTopCel, new Position(1, 8), new Position(1, null), pictureValue);
        PoiUtils.writePicture(sAnchorToLeftTopCel, new Position(1, 16), new Position(null, 1), pictureValue);

        try (OutputStream outputStream = super.createOutputStream(false, SheetFileType.TYPE_XLSX)) {
            workbook.write(outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCopyCells() {
        try (InputStream insXls = loadTemplate(SheetFileType.TYPE_XLS)) {
            Workbook workbook = PoiUtils.initWorkbook(insXls, SheetFileType.TYPE_XLS);
            Sheet sheet = workbook.getSheetAt(0);

            // copy single cell to larger area
            PoiUtils.copyCells(sheet, new Area(new Position(0,0)), new Area(new Position(8, 8), new Position(9, 9)));

            // copy 3x3 to 11x11
            PoiUtils.copyCells(sheet, new Area(1, 1, 3, 3), new Area(10, 10, 20, 20));

            // copy first cell to line contains it.
            Translator translator = new Translator();
            PoiUtils.copyCells(sheet, translator.toArea("M1"), translator.toArea("M1:Q1"));

            // copy large area to small area
            PoiUtils.copyCells(sheet, Area.newArea(new Position(0,0), 5, 5),
                    new Area(new Position(15, 0), new Position(17, 2)));

            try (OutputStream outputStream = super.createOutputStream(true, SheetFileType.TYPE_XLS, "copy_cells")) {
                workbook.write(outputStream);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
