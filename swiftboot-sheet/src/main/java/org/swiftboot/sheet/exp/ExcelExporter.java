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

        exportMeta.accept(new MetaVisitor() {
            @Override
            public void visitSingleCell(String key, Position position) {
                extendSheet(sheet, position);
                Row row = sheet.getRow(position.getRow());
                Cell cell = row.getCell(position.getColumn());
                Object value = exportMeta.getValue(key);
                setValueToCell(cell, value);
            }

            @Override
            public void visitHorizontalLine(String key, Position startPos, int columnCount) {
                extendSheet(sheet, startPos.clone().moveColumns(columnCount));
                Row row = sheet.getRow(startPos.getRow());
                List<Object> values = (List<Object>) exportMeta.getValue(key);
                setValuesToRow(row, startPos, columnCount, values);
            }

            @Override
            public void visitVerticalLine(String key, Position startPos, int rowCount) {
                extendSheet(sheet, startPos.clone().moveRows(rowCount));
                List<Object> values = (List<Object>) exportMeta.getValue(key);
                for (int i = 0; i < rowCount; i++) {
                    Row row = sheet.getRow(startPos.getRow() + i);
                    if (row != null) {
                        Cell cell = row.getCell(startPos.getColumn());
                        setValueToCell(cell, values.get(i));
                    }
                }
            }

            @Override
            public void visitMatrix(String key, Position startPos, int rowCount, int columnCount) {
                extendSheet(sheet, startPos.clone().moveRows(rowCount));
                extendSheet(sheet, startPos.clone().moveColumns(columnCount));
                List<List<Object>> matrix = (List<List<Object>>) exportMeta.getValue(key);
                for (int i = 0; i < rowCount; i++) {
                    List<Object> values = matrix.get(i);
                    Row row = sheet.getRow(startPos.getRow() + i);
                    setValuesToRow(row, startPos, columnCount, values);
                }
            }
        });
        wb.write(outputStream);
    }

    void extendSheet(Sheet sheet, Position position) {
        System.out.println("Last row num " + sheet.getLastRowNum());
        System.out.println("Physical number of rows " + sheet.getPhysicalNumberOfRows());
        System.out.println("Extend sheet to " + position);
        // Calculate the original size of a row.
        int originRowSize = 0;
        if (sheet.getRow(0) != null) {
            Row row = sheet.getRow(0);
            originRowSize = row.getLastCellNum() + 1;
        }

        int moreCols = originRowSize - position.getColumn();
        int moreRows = sheet.getLastRowNum() - position.getRow();

        // Extend columns TODO 需要考虑row=0的情况
        if (moreCols <= 0) {
            int columnCount = position.getColumn() - originRowSize;
            for (int i = 0; i < sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                // Append more columns to a row
                for (int j = 0; j < columnCount; j++) {
                    row.createCell(originRowSize + j);
                }
            }
        }

        // Extend rows
        if (moreRows <= 0) {
            int rowCount = position.getRow() + 1 - sheet.getPhysicalNumberOfRows();
            int rowSize = Math.max(originRowSize, position.getColumn() + 1);
            for (int i = 0; i < rowCount; i++) {
                Row newRow = sheet.createRow(i);
                for (int j = 0; j < rowSize; j++) {
                    newRow.createCell(j);
                }
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
