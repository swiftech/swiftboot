package org.swiftboot.sheet.util;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.swiftboot.sheet.constant.SheetFileType;
import org.swiftboot.sheet.meta.Picture;
import org.swiftboot.sheet.meta.Position;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

/**
 * @author allen
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
     * @param endPosition   end position of area, not null.
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
                anchor.setRow2(endPosition.getRow() + 1); // eclusive
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
}
