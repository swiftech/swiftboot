package org.swiftboot.sheet.exp;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.swiftboot.sheet.excel.ExcelCellInfo;
import org.swiftboot.sheet.excel.ExcelSheetInfo;
import org.swiftboot.sheet.meta.Picture;
import org.swiftboot.sheet.meta.*;
import org.swiftboot.sheet.util.PoiUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;


/**
 * Export data into a new or a templated Excel 2003/2007 file.
 *
 * @author swiftech
 */
public class ExcelExporter extends BaseExporter {

    private final Logger log = LoggerFactory.getLogger(ExcelExporter.class);

    private final ThreadLocal<ExcelCellInfo> cellInfo = new ThreadLocal<>();

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

        final AtomicReference<Sheet> sheetRef = new AtomicReference<>();
        final AtomicReference<Position> maxPositionRef = new AtomicReference<>(); // max position user wants
        exportMeta.setAllowFreeSize(true);
        exportMeta.accept(sheetId -> {
            log.debug("Export to sheet: " + sheetId);
            sheetRef.set(PoiUtils.getOrCreateSheet(wb, sheetId));
            cellInfo.get().setSheet(sheetRef.get());
            maxPositionRef.set(exportMeta.findMaxPosition(sheetId));
            extendSheet(sheetRef.get(), maxPositionRef.get());
            // callback to user client to handle the sheet.
            if (exportMeta.getSheetHandler(sheetId) != null) {
                ExcelSheetInfo sheetInfo = new ExcelSheetInfo(wb, sheetRef.get());
                SheetHandler<ExcelSheetInfo> handler = (SheetHandler<ExcelSheetInfo>) exportMeta.getSheetHandler(sheetId);
                handler.onSheet(sheetInfo);
            }
        }, (metaItem, startPos, rowCount, columnCount) -> {
            Sheet sheet = sheetRef.get();

            // merge all cells in the specific area.
            if (metaItem.isMerge() && !metaItem.getArea().isDynamic() && (rowCount > 1 || columnCount > 1)) {
                Cell firstCell = PoiUtils.getCell(sheet, startPos);
                CellRangeAddress merged = new CellRangeAddress(startPos.getRow(), startPos.getRow() + rowCount - 1,
                        startPos.getColumn(), startPos.getColumn() + columnCount - 1);
                log.debug(String.format("Merge cells: %s", merged.formatAsString()));
                sheet.addMergedRegion(merged);
                CellStyle cellStyle = firstCell.getCellStyle();
                cellStyle.setAlignment(HorizontalAlignment.LEFT);
                cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
                RegionUtil.setBorderTop(cellStyle.getBorderTop(), merged, sheet);
                RegionUtil.setBorderBottom(cellStyle.getBorderBottom(), merged, sheet);
                RegionUtil.setBorderLeft(cellStyle.getBorderLeft(), merged, sheet);
                RegionUtil.setBorderRight(cellStyle.getBorderRight(), merged, sheet);
            }

            if (metaItem.getValue() instanceof PictureLoader) {
                try {
                    Picture pictureValue = ((PictureLoader) metaItem.getValue()).get();
                    PoiUtils.writePicture(sheetRef.get(), startPos, startPos.clone().moveRows(rowCount).moveColumns(columnCount), pictureValue);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            else {
                List<List<Object>> matrix = asMatrix(metaItem.getValue(), rowCount, columnCount);
                if (matrix.isEmpty()) {
                    return;
                }
                int actualRowCount = rowCount == null ? matrix.size() : Math.min(rowCount, matrix.size());
                int actualColumnCount = columnCount == null ? matrix.get(0).size() : Math.min(columnCount, matrix.get(0).size());

                // shift rows if need insert
                if (metaItem.isInsert()) {
                    int insertRowCount = (rowCount == null || metaItem.isInsertByValue()) ? actualRowCount : rowCount;
                    log.debug(String.format("Insert %d rows start at row %d", insertRowCount, startPos.getRow()));
                    sheet.shiftRows(startPos.getRow(), sheet.getLastRowNum(), insertRowCount, true, true);
                    createCells(sheet, startPos, startPos.clone().moveRows(insertRowCount - 1).moveColumns(maxPositionRef.get().getColumn() - 1), 0, 0);
                }

                if (metaItem.getCopyArea() != null && !metaItem.getCopyArea().isDynamic()) {
                    // if row or column is uncertain size, use copied area's row or column size instead.
                    Area targetArea = Area.newArea(startPos,
                            rowCount == null ? metaItem.getCopyArea().rowCount() : rowCount,
                            columnCount == null ? metaItem.getCopyArea().columnCount() : columnCount);
                    log.debug(String.format("Copy cells from %s to %s", metaItem.getCopyArea(), targetArea));
                    PoiUtils.copyCells(sheet, metaItem.getCopyArea(), targetArea);
                }

                if (metaItem.isMerge()) {
                    // merge values into lines
                    Optional<String> optMergeValues = matrix.stream().map(objects -> StringUtils.join(objects, ","))
                            .reduce((s, s2) -> String.format("%s\n%s", s, s2));
                    if (optMergeValues.isPresent()) {
                        Cell cell = sheet.getRow(startPos.getRow()).getCell(startPos.getColumn());
                        PoiUtils.setValueToCell(cell, optMergeValues.get());
                    }
                }
                else {
                    for (int i = 0; i < actualRowCount; i++) {
                        cellInfo.get().setRowIdx(i);
                        List<Object> values = matrix.get(i);
                        Row row = sheetRef.get().getRow(startPos.getRow() + i);
                        setValuesToRow(row, startPos.clone().moveRows(i), actualColumnCount, values, (CellHandler<ExcelCellInfo>) metaItem.getCellHandler());
                    }
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
        int originRowSize = 0; // get how many columns from first row
        if (sheet.getRow(0) != null) {
            Row row = sheet.getRow(0);
            originRowSize = row.getLastCellNum();
        }
        log.debug(String.format("Original row count: %d", originRowCount));
        log.debug(String.format("Original physical number of rows: %d", sheet.getPhysicalNumberOfRows()));
        log.debug(String.format("Original row size: %d", originRowSize));

        createCells(sheet, new Position(0, 0), lastPosition, originRowCount, originRowSize);

        log.debug(String.format("Extended row count: %d", sheet.getPhysicalNumberOfRows()));

        checkExtendingResultForSheet(sheet, lastPosition);
    }

    /**
     * Create cells from startPosition to lastPosition in sheet.
     * originRowCount and originRowSize are required to avoid cover existed cells.
     *
     * @param sheet
     * @param startPosition
     * @param lastPosition
     * @param originRowCount
     * @param originRowSize
     */
    void createCells(Sheet sheet, Position startPosition, Position lastPosition, int originRowCount, int originRowSize) {
        // Extend columns first for existent rows.
        int expectRowSize = lastPosition.getColumn() + 1;
        int moreCols = expectRowSize - originRowSize;
        if (moreCols > 0) {
            for (int i = startPosition.getRow(); i < originRowCount; i++) {
                Row row = sheet.getRow(i);
                if (row == null) {
                    row = sheet.createRow(i);
                }
                // Append more columns to a row
                for (int j = startPosition.getRow(); j < moreCols; j++) {
                    row.createCell(originRowSize + j);
                }
            }
        }

        // Extend rows
        int moreRows = lastPosition.getRow() + 1 - originRowCount;
        if (moreRows > 0) {
            int actualRowSize = Math.max(originRowSize, expectRowSize);
            for (int i = startPosition.getRow(); i < moreRows; i++) {
                Row newRow = sheet.createRow(originRowCount + i);
                for (int j = startPosition.getColumn(); j < actualRowSize; j++) {
                    newRow.createCell(j);
                }
            }
        }
    }

    /**
     * Check the extending is ok for export.
     *
     * @param sheet
     * @param lastPosition
     */
    private void checkExtendingResultForSheet(Sheet sheet, Position lastPosition) {
        // Check extending result
        if (sheet.getLastRowNum() < 0 || sheet.getLastRowNum() < lastPosition.getRow()) {
            throw new RuntimeException(String.format("Extending sheet rows inappropriate: %d", sheet.getLastRowNum()));
        }
        else {
            if (sheet.getRow(0) != null) {
                short lastCellNum = sheet.getRow(0).getLastCellNum(); // getLastCellNum is 1-based
                // check the last extended column is as user's request.
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
            Object cellValue = values.get(j);
            PoiUtils.setValueToCell(cell, cellValue);
            cellInfo.get().setCell(cell);
            cellInfo.get().setValue(cellValue);
            if (cellHandler != null) {
                cellHandler.onCell(cellInfo.get());
            }
        }
    }

}
