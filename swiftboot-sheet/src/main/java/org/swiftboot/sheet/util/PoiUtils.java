package org.swiftboot.sheet.util;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.swiftboot.sheet.constant.SheetFileType;

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
}
