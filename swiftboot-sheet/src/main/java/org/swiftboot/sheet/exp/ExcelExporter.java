package org.swiftboot.sheet.exp;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.swiftboot.sheet.meta.*;
import org.swiftboot.sheet.util.PoiUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import static org.swiftboot.sheet.util.PoiUtils.setValueToCell;

/**
 * Export data into a new or a templated Excel 2003/2007 file.
 *
 * @author allen
 */
public class ExcelExporter extends BaseExporter {

    public ExcelExporter(String fileType) {
        super(fileType);
    }

    @Override
    public <T> void export(Object dataObject, OutputStream outputStream) throws IOException {
        this.export(null, dataObject, outputStream);
    }

    @Override
    public <T> void export(InputStream templateFileStream, Object dataObject, OutputStream outputStream) throws IOException {
        SheetMetaBuilder builder = new SheetMetaBuilder();
        SheetMeta meta = builder.fromAnnotatedObject(dataObject).build();
//        SheetMeta meta = new SheetMeta();
//        meta.fromAnnotatedObject(dataObject);
        this.export(templateFileStream, meta, outputStream);
    }

    @Override
    public void export(SheetMeta exportMeta, OutputStream outputStream) throws IOException {
        this.export(null, exportMeta, outputStream);
    }

    @Override
    public void export(InputStream templateFileStream, SheetMeta exportMeta, OutputStream outputStream) throws IOException {
        Workbook wb = PoiUtils.initWorkbook(templateFileStream, super.getFileType());
        Sheet sheet = PoiUtils.firstSheet(wb);

        this.extendSheet(sheet, exportMeta.findMaxPosition());

        exportMeta.setAllowFreeSize(true);
        exportMeta.accept((key, startPos, rowCount, columnCount, value) -> {
//            Object value = exportMeta.getValue(key);
            if (value instanceof PictureLoader) {
                try {
                    Picture pictureValue = ((PictureLoader) value).get();
                    PoiUtils.writePicture(sheet, startPos, startPos.clone().moveRows(rowCount).moveColumns(columnCount), pictureValue);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            else {
                List<List<Object>> matrix = asMatrix(value, rowCount, columnCount);
                int actualRowCount = rowCount == null ? matrix.size() : Math.min(rowCount, matrix.size());
                int actualColumnCount = columnCount == null ? matrix.get(0).size() : Math.min(columnCount, matrix.get(0).size());
                for (int i = 0; i < actualRowCount; i++) {
                    List<Object> values = matrix.get(i);
                    Row row = sheet.getRow(startPos.getRow() + i);
                    setValuesToRow(row, startPos.clone().moveRows(i), actualColumnCount, values);
                }

            }
        });
        wb.write(outputStream);
    }

    /**
     * Extends the sheet by last cell position.
     *
     * @param sheet
     * @param lastPosition
     */
    void extendSheet(Sheet sheet, Position lastPosition) {
        if (lastPosition == null || lastPosition.isUncertain()) {
            return;
        }
        log.debug(String.format("Try to extend sheet to %s%n", lastPosition));
        // Calculate the original size of a row.
        int originRowCount = sheet.getLastRowNum() + 1;
        int originRowSize = 0;
        if (sheet.getRow(0) != null) {
            Row row = sheet.getRow(0);
            originRowSize = row.getLastCellNum() + 1;
        }
        log.debug(String.format("Original row count: %d%n", originRowCount));
        log.debug(String.format("Original physical number of rows: %d%n", sheet.getPhysicalNumberOfRows()));
        log.debug(String.format("Original row size: %d%n", originRowSize));

        // Extend columns
        int moreCols = lastPosition.getColumn() + 1 - originRowSize;
        if (moreCols > 0) {
            for (int i = 0; i < originRowCount; i++) {
                Row row = sheet.getRow(i);
                if (row == null) {
                    row = sheet.createRow(i);
//                    throw new RuntimeException(String.format("Row(index) %d cannot be null", i));
                }
                // Append more columns to a row
                for (int j = 0; j < moreCols; j++) {
                    row.createCell(originRowSize + j);
                }
            }
        }

        // Extend rows
        int moreRows = lastPosition.getRow() + 1 - originRowCount;
        if (moreRows > 0) {
            int actualRowSize = Math.max(originRowSize, lastPosition.getColumn() + 1);
            for (int i = 0; i < moreRows; i++) {
                Row newRow = sheet.createRow(originRowCount + i);
                for (int j = 0; j < actualRowSize; j++) {
                    newRow.createCell(j);
                }
            }
        }

        // Check extending result
        if (sheet.getLastRowNum() < 0 || sheet.getLastRowNum() < lastPosition.getRow()) {
            throw new RuntimeException(String.format("Extending sheet rows inappropriate: %d", sheet.getLastRowNum()));
        }
        else {
            if (sheet.getRow(0) != null) {
                short lastCellNum = sheet.getRow(0).getLastCellNum();
                if (lastCellNum < lastPosition.getColumn() + 1) {
                    throw new RuntimeException(String.format("Extending sheet column inappropriate: %d", lastCellNum));
                }
            }
            else {
                throw new RuntimeException(String.format("Extending sheet rows inappropriate: %d", sheet.getLastRowNum()));
            }
        }
    }

    private void setValuesToRow(Row row, Position startPos, int columnCount, List<Object> values) {
        if (row == null || startPos == null) {
            return;
        }
        int colCount = Math.min(Math.max(columnCount, 1), values.size()); // at least one and not more than size of data.
        for (int j = 0; j < colCount; j++) {
            Cell cell = row.getCell(startPos.getColumn() + j);
            if (cell == null) {
                cell = row.createCell(startPos.getColumn() + j);
            }
            setValueToCell(cell, values.get(j));
        }
    }

}
