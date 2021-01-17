package org.swiftboot.sheet.exp;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.swiftboot.sheet.meta.MetaVisitor;
import org.swiftboot.sheet.meta.Position;
import org.swiftboot.sheet.meta.SheetMeta;
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
        SheetMeta meta = new SheetMeta();
        meta.fromAnnotatedObject(dataObject);
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
        exportMeta.accept(new MetaVisitor() {
            @Override
            public void visitSingleCell(String key, Position position) {
                Row row = sheet.getRow(position.getRow());
                Cell cell = row.getCell(position.getColumn());
                Object value = exportMeta.getValue(key);
                setValueToCell(cell, value);
            }

            @Override
            public void visitHorizontalLine(String key, Position startPos, Integer columnCount) {
                List<Object> values = (List<Object>) exportMeta.getValue(key);
                System.out.println(columnCount);
                int actualCount = columnCount == null ? values.size() : Math.min(columnCount, values.size());
                Row row = sheet.getRow(startPos.getRow());
                setValuesToRow(row, startPos, actualCount, values);
            }

            @Override
            public void visitVerticalLine(String key, Position startPos, Integer rowCount) {
                List<Object> values = (List<Object>) exportMeta.getValue(key);
                int actualCount = rowCount == null ? values.size() : Math.min(rowCount, values.size());
                for (int i = 0; i < actualCount; i++) {
                    Row row = sheet.getRow(startPos.getRow() + i);
                    if (row != null) {
                        Cell cell = row.getCell(startPos.getColumn());
                        setValueToCell(cell, values.get(i));
                    }
                }
            }

            @Override
            public void visitMatrix(String key, Position startPos, Integer rowCount, Integer columnCount) {
                List<List<Object>> matrix = (List<List<Object>>) exportMeta.getValue(key);
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

    void extendSheet(Sheet sheet, Position position) {
        System.out.println("Try to extend sheet to " + position);
        // Calculate the original size of a row.
        int originRowCount = sheet.getLastRowNum() + 1;
        int originRowSize = 0;
        if (sheet.getRow(0) != null) {
            Row row = sheet.getRow(0);
            originRowSize = row.getLastCellNum() + 1;
        }
        System.out.println("Original row count: " + originRowCount);
        System.out.println("Physical number of rows: " + sheet.getPhysicalNumberOfRows());
        System.out.println("Original row size: " + originRowSize);

        // Extend columns TODO 需要考虑row=0的情况
        int moreCols = position.getColumn() + 1 - originRowSize;
        if (moreCols > 0) {
            for (int i = 0; i < originRowCount; i++) {
                Row row = sheet.getRow(i);
                // Append more columns to a row
                for (int j = 0; j < moreCols; j++) {
                    row.createCell(originRowSize + j);
                }
            }
        }

        // Extend rows
        int moreRows = position.getRow() + 1 - originRowCount;
        if (moreRows > 0) {
            int actualRowSize = Math.max(originRowSize, position.getColumn() + 1);
            for (int i = 0; i < moreRows; i++) {
                Row newRow = sheet.createRow(originRowCount + i);
                for (int j = 0; j < actualRowSize; j++) {
                    newRow.createCell(j);
                }
            }
        }

        // Check extending result
        if (sheet.getLastRowNum() < 0 || sheet.getLastRowNum() < position.getRow()) {
            throw new RuntimeException(String.format("Extending sheet rows inappropriate: %d", sheet.getLastRowNum()));
        }
        else {
            if (sheet.getRow(0) != null) {
                short lastCellNum = sheet.getRow(0).getLastCellNum();
                if (lastCellNum < position.getColumn() + 1) {
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
