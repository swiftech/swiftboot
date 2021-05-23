package org.swiftboot.sheet.exp;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.swiftboot.sheet.excel.ExcelCellInfo;
import org.swiftboot.sheet.meta.*;
import org.swiftboot.sheet.util.PoiUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;


/**
 * Export data into a new or a templated Excel 2003/2007 file.
 *
 * @author swiftech
 */
public class ExcelExporter extends BaseExporter {

    Logger log = LoggerFactory.getLogger(ExcelExporter.class);

    ThreadLocal<ExcelCellInfo> cellInfo = new ThreadLocal<>();

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
        this.export(templateFileStream, meta, outputStream);
    }

    @Override
    public void export(SheetMeta exportMeta, OutputStream outputStream) throws IOException {
        this.export(null, exportMeta, outputStream);
    }

    @Override
    public void export(InputStream templateFileStream, SheetMeta exportMeta, OutputStream outputStream) throws IOException {
        Workbook wb = PoiUtils.initWorkbook(templateFileStream, super.getFileType());
        cellInfo.set(new ExcelCellInfo());
        cellInfo.get().setWorkbook(wb);

        final AtomicReference<Sheet> sheet = new AtomicReference<>();
        exportMeta.setAllowFreeSize(true);
        exportMeta.accept(sheetId -> {
            log.debug("Export to sheet: " + sheetId);
            sheet.set(PoiUtils.getOrCreateSheet(wb, sheetId));
            cellInfo.get().setSheet(sheet.get());
            extendSheet(sheet.get(), exportMeta.findMaxPosition(sheetId));
        }, (key, startPos, rowCount, columnCount, value, cellHandler) -> {
            if (value instanceof PictureLoader) {
                try {
                    Picture pictureValue = ((PictureLoader) value).get();
                    PoiUtils.writePicture(sheet.get(), startPos, startPos.clone().moveRows(rowCount).moveColumns(columnCount), pictureValue);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            else {
                List<List<Object>> matrix = asMatrix(value, rowCount, columnCount);
                if (matrix.isEmpty()) {
                    return;
                }
                int actualRowCount = rowCount == null ? matrix.size() : Math.min(rowCount, matrix.size());
                int actualColumnCount = columnCount == null ? matrix.get(0).size() : Math.min(columnCount, matrix.get(0).size());
                for (int i = 0; i < actualRowCount; i++) {
                    cellInfo.get().setRowIdx(i);
                    List<Object> values = matrix.get(i);
                    Row row = sheet.get().getRow(startPos.getRow() + i);
                    setValuesToRow(row, startPos.clone().moveRows(i), actualColumnCount, values, (CellHandler<ExcelCellInfo>) cellHandler);
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
        log.debug(String.format("Try to extend sheet to %s", lastPosition));
        // Calculate the original size of a row.
        int originRowCount = sheet.getLastRowNum() + 1;
        int originRowSize = 0;
        if (sheet.getRow(0) != null) {
            Row row = sheet.getRow(0);
            originRowSize = row.getLastCellNum() + 1;
        }
        log.debug(String.format("Original row count: %d", originRowCount));
        log.debug(String.format("Original physical number of rows: %d", sheet.getPhysicalNumberOfRows()));
        log.debug(String.format("Original row size: %d", originRowSize));

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


    /**
     * @param row
     * @param startPos
     * @param columnCount how many columns to write value, could be greater or less than data size.
     * @param values
     * @param cellHandler
     */
    private void setValuesToRow(Row row, Position startPos, int columnCount, List<Object> values, CellHandler<ExcelCellInfo> cellHandler) {
        if (row == null || startPos == null) {
            return;
        }
        int colCount = Math.min(Math.max(columnCount, 1), values.size()); // at least one and not more than size of data.
        for (int j = 0; j < colCount; j++) {
            cellInfo.get().setColIdx(j);
            Cell cell = row.getCell(startPos.getColumn() + j);
            if (cell == null) {
                cell = row.createCell(startPos.getColumn() + j);
            }
            PoiUtils.setValueToCell(cell, values.get(j));
            cellInfo.get().setCell(cell);
            if (cellHandler != null) {
                cellHandler.onCell(cellInfo.get());
            }
        }
    }

}
