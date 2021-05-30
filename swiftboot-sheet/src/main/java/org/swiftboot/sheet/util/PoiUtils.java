package org.swiftboot.sheet.util;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.WorkbookUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.swiftboot.collections.Matrix;
import org.swiftboot.sheet.constant.SheetFileType;
import org.swiftboot.sheet.meta.Area;
import org.swiftboot.sheet.meta.Picture;
import org.swiftboot.sheet.meta.Position;
import org.swiftboot.sheet.meta.SheetId;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author swiftech
 */
public class PoiUtils {

    /**
     * Get first sheet from a worksheet file by it's suffix
     *
     * @param templateFileStream
     * @param fileSuffix
     * @return
     * @throws IOException
     */
    public static Sheet firstSheet(InputStream templateFileStream, String fileSuffix) throws IOException {
        Workbook workbook = initWorkbook(templateFileStream, fileSuffix);
        return firstSheet(workbook);
    }

    /**
     * Get first sheet of workbook, create new if not existed.
     *
     * @param workbook
     * @return
     */
    public static Sheet firstSheet(Workbook workbook) {
        int sheetCount = workbook.getNumberOfSheets();
        if (sheetCount <= 0) {
            return workbook.createSheet();
        }
        else {
            return workbook.getSheetAt(0);
        }
    }

    /**
     * Get sheet from a worksheet file by it's suffix
     *
     * @param templateFileStream
     * @param fileSuffix
     * @return
     * @throws IOException
     */
    public static Sheet getSheet(InputStream templateFileStream, String fileSuffix, int sheetIdx) throws IOException {
        Workbook workbook = initWorkbook(templateFileStream, fileSuffix);
        return getSheet(workbook, sheetIdx);
    }

    /**
     * Get sheet of workbook by index.
     *
     * @param workbook
     * @param sheetIdx
     * @return
     */
    public static Sheet getSheet(Workbook workbook, int sheetIdx) {
        int sheetCount = workbook.getNumberOfSheets();
        if (sheetCount <= sheetIdx) {
            return null;
        }
        else {
            return workbook.getSheetAt(sheetIdx);
        }
    }

    /**
     * Try to get sheet by index and name one by one.
     * If found by index, sheet name will be overwritten by specified name.
     * If not found, new sheet will be created.
     *
     * @param workbook
     * @param sheetId
     * @return
     */
    public static Sheet getOrCreateSheet(Workbook workbook, SheetId sheetId) {
        if (sheetId == null) {
            throw new RuntimeException("Sheet id can not be null");
        }
        Sheet sheet = null;
        if (sheetId.getSheetIndex() == null || sheetId.getSheetIndex() >= workbook.getNumberOfSheets()) {
            if (StringUtils.isBlank(sheetId.getSheetName())) {
                if (sheetId.getSheetIndex() == null) {
                    throw new RuntimeException("Sheet id is not valid: " + sheetId);
                }
                else {
                    return workbook.createSheet(SheetId.DEFAULT_SHEET_NAME);
                }
            }
            else {
                return getOrCreateSheet(workbook, sheetId.getSheetName());
            }
        }
        else {
            sheet = workbook.getSheetAt(sheetId.getSheetIndex());
            if (sheet == null) {
                if (StringUtils.isBlank(sheetId.getSheetName())) {
                    return workbook.createSheet(); //
                }
                else {
                    return getOrCreateSheet(workbook, sheetId.getSheetName());
                }
            }
            else {
                if (StringUtils.isNotBlank(sheetId.getSheetName())) {
                    String safeSheetName = WorkbookUtil.createSafeSheetName(sheetId.getSheetName());
                    workbook.setSheetName(workbook.getSheetIndex(sheet), safeSheetName);
                }
                return sheet;
            }
        }
    }

    /**
     * Get sheet of workbook by name, create new if not exist.
     *
     * @param workbook
     * @param sheetName
     * @return
     */
    public static Sheet getOrCreateSheet(Workbook workbook, String sheetName) {
        String safeSheetName = WorkbookUtil.createSafeSheetName(sheetName);
        Sheet sheet = workbook.getSheet(safeSheetName);
        if (sheet == null) {
            return workbook.createSheet(safeSheetName);
        }
        else {
            return sheet;
        }
    }

    /**
     * Read from excel file stream to get the workbook, create a new one if not exists.
     *
     * @param templateFileStream
     * @param fileSuffix
     * @return
     * @throws IOException
     */
    public static Workbook initWorkbook(InputStream templateFileStream, String fileSuffix) throws IOException {
        if (SheetFileType.TYPE_XLS.equals(fileSuffix)) {
            return templateFileStream == null ? new HSSFWorkbook() : new HSSFWorkbook(templateFileStream);
        }
        else if (SheetFileType.TYPE_XLSX.equals(fileSuffix)) {
            return templateFileStream == null ? new XSSFWorkbook() : new XSSFWorkbook(templateFileStream);
        }
        else {
            throw new RuntimeException(String.format("%s file is not supported", fileSuffix));
        }
    }


    public static Cell getCell(Sheet sheet, Position position) {
        return getCell(sheet, position.getRow(), position.getColumn());
    }

    public static Cell getCell(Sheet sheet, int rowIdx, int colIdx) {
        return sheet.getRow(rowIdx).getCell(colIdx);
    }


    public static Object getValueFromCell(Cell cell) {
        if (cell != null) {
            switch (cell.getCellType()) {
                case STRING:
                case FORMULA:
                    return cell.getStringCellValue();
                case NUMERIC:
                    if (DateUtil.isCellDateFormatted(cell)) {
                        return cell.getDateCellValue();
                    }
                    else {
                        return cell.getNumericCellValue();
                    }
                case BLANK:
                    break;
                case BOOLEAN:
                    return cell.getBooleanCellValue();
                default:
                    return null;
            }
        }
        return null;
    }


    public static void setValueToCell(Cell cell, Object value) {
        if (ObjectUtils.allNotNull(cell, value)) {
            if (value instanceof String) {
                cell.setCellValue(String.valueOf(value));
            }
            else if (value instanceof Number) {
                cell.setCellValue(((Number) value).doubleValue());
            }
            else if (value instanceof Boolean) {
                cell.setCellValue((Boolean) value);
            }
            else if (value instanceof Date) {
                cell.setCellValue((Date) value);
            }
            else if (value instanceof LocalDateTime) {
                cell.setCellValue((LocalDateTime) value);
            }
            else if (value instanceof LocalDate) {
                cell.setCellValue((LocalDate) value);
            }
            else if (value instanceof Calendar) {
                cell.setCellValue(((Calendar) value).getTime());
            }
            else {
                cell.setCellValue(value.toString());
            }
        }
        else {
            System.out.printf("Cell or value is null: %s - %s%n", cell, value);
        }
    }

    /**
     * Write picture (in bytes) to a sheet in cells area (if specifically),
     * otherwise, the picture will be anchored to the start position cell and draws as is (actually be stretched somehow).
     *
     * @param sheet
     * @param startPos
     * @param endPosition  end position of area, not null.
     * @param pictureValue
     */
    public static void writePicture(Sheet sheet, Position startPos, Position endPosition, Picture pictureValue) {
        if (ObjectUtils.allNotNull(sheet, startPos, pictureValue)) {
            // System.out.printf("Write picture from %s to %s%n", startPos, endPosition);
            Workbook wb = sheet.getWorkbook();

            int pictureIdx = wb.addPicture(pictureValue.getData(), pictureValue.getPoiPictureType());

            CreationHelper helper = wb.getCreationHelper();
            Drawing drawing = sheet.createDrawingPatriarch();
            ClientAnchor anchor = helper.createClientAnchor();

            boolean isRestrictedInArea = true;
            if (endPosition == null || endPosition.isUncertain()) {
                isRestrictedInArea = false;
            }
            // System.out.printf("%s restrict in area%n", isRestrictedInArea ? "" : "not ");

            // set top-left corner of the picture,
            // subsequent call of Picture#resize() will operate relative to it
            anchor.setCol1(startPos.getColumn());
            anchor.setRow1(startPos.getRow());
            if (isRestrictedInArea) {
                anchor.setCol2(endPosition.getColumn() + 1); // exclusive
                anchor.setRow2(endPosition.getRow() + 1); // exclusive
            }
            anchor.setDx1(0);
            anchor.setDy1(0);
            anchor.setDx2(1023);
            anchor.setDy2(255);
            if (isRestrictedInArea) {
                anchor.setAnchorType(ClientAnchor.AnchorType.MOVE_AND_RESIZE); // dynamic size
            }
            else {
                anchor.setAnchorType(ClientAnchor.AnchorType.MOVE_DONT_RESIZE); // absolute size, should resize POI Picture.
            }
//            System.out.println(anchor);
            org.apache.poi.ss.usermodel.Picture pict = drawing.createPicture(anchor, pictureIdx);
            if (!isRestrictedInArea) {
                pict.resize();
            }
        }
        else {
            System.out.printf("Sheet or position or value is null: %s - %s - %s%n", sheet, startPos, pictureValue);
        }
    }

    /**
     * Copy style of area 'from' to area 'to' repeatedly.
     *
     * @param sheet
     * @param from area copied from must has fixed size.
     * @param to area copy to must has fixed size
     */
    public static void copyCells(Sheet sheet, Area from, Area to) {
        // collect all cells style
        Matrix<CellStyle> mFrom = new Matrix<>(from.rowCount(), from.columnCount());
        List<Float> rowHeights = new ArrayList<>(from.rowCount());
        for (int i = 0; i < from.rowCount(); i++) {
            int rowIdx = from.getStartPosition().getRow() + i;
            Row row = sheet.getRow(rowIdx);
            rowHeights.add(row.getHeightInPoints());
            for (int j = 0; j < from.columnCount(); j++) {
                int colIdx = from.getStartPosition().getColumn() + j;
                Cell cell = row.getCell(colIdx);
                if (cell == null) {
                    throw new RuntimeException(String.format("No cell found at (%d - %d) to copy from", rowIdx, colIdx));
                }
                mFrom.set(i, j, cell.getCellStyle());
            }
        }

        int rowMultiple = to.rowCount() / from.rowCount() + 1; //
        int colMultiple = to.columnCount() / from.columnCount() + 1;
        for (int x = 0; x < rowMultiple; x++) {
            for (int i = 0; i < from.rowCount(); i++) {
                int rowStep = x * from.rowCount();
                int newRowIdx = rowStep + i; // not global index
                if (newRowIdx >= to.rowCount()) {
                    continue;
                }
                Row copyRow = sheet.getRow(to.getStartPosition().getRow() + newRowIdx);
                if (copyRow == null) {
                    copyRow = sheet.createRow(to.getStartPosition().getRow() + newRowIdx);
                }
                copyRow.setHeightInPoints(rowHeights.get(i));
                for (int y = 0; y < colMultiple; y++) {
                    for (int j = 0; j < from.columnCount(); j++) {
                        int colStep = y * from.rowCount();
                        int newColIdx = colStep + j; // not global index
                        if (newColIdx >= to.columnCount()) {
                            continue;
                        }
                        Cell newCell = copyRow.createCell(to.getStartPosition().getColumn() + newColIdx);
                        CellStyle copiedStyle = mFrom.get(i, j);
                        CellStyle copyStyle = sheet.getWorkbook().createCellStyle();
                        copyStyle.cloneStyleFrom(copiedStyle);
                        newCell.setCellStyle(copyStyle);
                    }
                }
            }
        }
    }

}
