package org.swiftboot.sheet.imp;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.swiftboot.sheet.meta.Position;
import org.swiftboot.sheet.meta.SheetMeta;
import org.swiftboot.sheet.util.PoiUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.swiftboot.sheet.util.PoiUtils.getValueFromCell;

/**
 * Importer for Microsoft Microsoft Excel 97-2003 and 2007
 *
 * @author allen
 */
public class ExcelImporter extends BaseImporter {

    public ExcelImporter(String fileType) {
        super(fileType);
    }

    @Override
    public Map<String, Object> importFromStream(InputStream templateFileStream, SheetMeta meta) throws IOException {
        Workbook wb = PoiUtils.initWorkbook(templateFileStream, super.getFileType());
        Sheet sheet = wb.getSheetAt(0);
//        Sheet sheet = PoiUtils.getSheet(templateFileStream, super.getFileType(), 0);
//        int sheetCount = wb.getNumberOfSheets();
//        if (sheetCount > 0) {
            Map<String, Object> ret = new HashMap<>();
//            Sheet dataSheet = wb.getSheetAt(0);

            meta.accept((key, startPos, rowCount, columnCount, v) -> {
                List<List<Object>> matrix = new ArrayList<>();
                for (int i = 0; i < rowCount; i++) {
                    Row row = sheet.getRow(startPos.getRow() + i);
                    matrix.add(getValuesInRow(row, startPos, columnCount));
                }
                Object value = shrinkMatrix(matrix, rowCount, columnCount);
                ret.put(key, value);
            });
            return ret;
//        }
//        return null;
    }

    private List<Object> getValuesInRow(Row row, Position startPos, int columnCount) {
        if (row == null || startPos == null) {
            return null;
        }
        int colCount = Math.max(columnCount, 1);
        List<Object> values = new ArrayList<>();
        for (int j = 0; j < colCount; j++) {
            Cell cell = row.getCell(startPos.getColumn() + j);
            values.add(getValueFromCell(cell));
        }
        return values;
    }

}
