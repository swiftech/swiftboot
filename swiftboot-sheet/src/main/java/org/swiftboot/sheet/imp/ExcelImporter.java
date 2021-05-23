package org.swiftboot.sheet.imp;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static org.swiftboot.sheet.util.PoiUtils.getValueFromCell;

/**
 * Importer for Microsoft Microsoft Excel 97-2003 and 2007
 *
 * @author swiftech
 */
public class ExcelImporter extends BaseImporter {

    Logger log = LoggerFactory.getLogger(ExcelImporter.class);

    ThreadLocal<ExcelCellInfo> cellInfo = new ThreadLocal<>();

    public ExcelImporter(String fileType) {
        super(fileType);
    }

    @Override
    public Map<String, Object> importFromStream(InputStream templateFileStream, SheetMeta meta) throws IOException {
        Workbook wb = PoiUtils.initWorkbook(templateFileStream, super.getFileType());
        cellInfo.set(new ExcelCellInfo());
        cellInfo.get().setWorkbook(wb);

        Map<String, Object> ret = new HashMap<>();

        final AtomicReference<Sheet> sheet = new AtomicReference<>();
        meta.accept(sheetId -> {
            log.trace("Sheet: " + sheetId);
            sheet.set(PoiUtils.getSheet(wb, sheetId.getSheetIndex()));
            if (sheet.get() == null) {
                log.warn("No sheet found: " + sheetId);
                return;
            }
            cellInfo.get().setSheet(sheet.get());
        }, (key, startPos, rowCount, columnCount, v, cellHandler) -> {
            log.trace(String.format("Item: '%s' %s-%s-%s", key, startPos, rowCount, columnCount));
            List<List<Object>> matrix = new ArrayList<>();
            for (int i = 0; i < rowCount; i++) {
                cellInfo.get().setRowIdx(i);
                int rowIdx = startPos.getRow() + i;
                Row row = sheet.get().getRow(rowIdx);
                if (row == null) {
                    log.warn("No row found at " + rowIdx);
                    continue;
                }
                matrix.add(getValuesInRow(row, startPos, columnCount, (CellHandler<ExcelCellInfo>) cellHandler));
            }
            Object value = shrinkMatrix(matrix, rowCount, columnCount);
            ret.put(key, value);
        });

        return ret;
    }

    private List<Object> getValuesInRow(Row row, Position startPos, int columnCount, CellHandler<ExcelCellInfo> cellHandler) {
        if (row == null || startPos == null) {
            return null;
        }
        int colCount = Math.max(columnCount, 1);
        List<Object> values = new ArrayList<>();
        for (int j = 0; j < colCount; j++) {
            Cell cell = row.getCell(startPos.getColumn() + j);
            values.add(getValueFromCell(cell));
            cellInfo.get().setCell(cell);
            if (cellHandler != null) {
                cellHandler.onCell(cellInfo.get());
            }
        }
        return values;
    }

}
