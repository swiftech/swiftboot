package org.swiftboot.sheet.imp;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.text.StringTokenizer;
import org.swiftboot.sheet.CsvCellInfo;
import org.swiftboot.sheet.meta.Position;
import org.swiftboot.sheet.meta.SheetMeta;
import org.swiftboot.util.IoUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Importer for CSV data sheet.
 *
 * @author swiftech
 */
public class CsvImporter extends BaseImporter {

    public CsvImporter(String fileType) {
        super(fileType);
    }

    @Override
    public Map<String, Object> importFromStream(InputStream templateFileStream, SheetMeta meta) throws IOException {
        // read lines
        List<String> lines = IoUtils.readToStringList(templateFileStream);
        Map<String, Object> ret = new HashMap<>();
        meta.accept((metaItem, startPos, rowCount, columnCount) -> {
            List<List<Object>> matrix = new ArrayList<>();
            boolean foundTarget = false;
            for (int i = 0; i < lines.size(); i++) {
                String row;
                if (isStaticWay(metaItem)
                        && (i < startPos.getRow() || i >= (startPos.getRow() + rowCount))) {
                    continue; // skip for static way.
                }
                else {
                    if (startPos != null && i >= (startPos.getRow() + rowCount)) {
                        break;
                    }
                    row = lines.get(i); // retrieve to be detected for predicate way.
                }
                StringTokenizer valueTokenizer = StringTokenizer.getCSVInstance(row);
                List<Object> values = new ArrayList<>();
                int cursor = 0;
                while (valueTokenizer.hasNext()) {
                    String next = valueTokenizer.next();
                    if (!foundTarget && metaItem.getPredicate() != null) {
                        CsvCellInfo csvCellInfo = new CsvCellInfo(next);
                        if (!metaItem.getPredicate().test(csvCellInfo)) {
                            cursor++;
                            continue; // not found the anchor cell and keep processing.
                        }
                        foundTarget = true;
                        startPos = new Position(i, cursor);
                    }
                    if (cursor >= startPos.getColumn()
                            && cursor < startPos.getColumn() + columnCount) {
                        values.add(next);
                        if (metaItem.getCellHandler() != null) {
                            CsvCellInfo csvCellInfo = new CsvCellInfo(next);
                            ((Consumer<CsvCellInfo>) metaItem.getCellHandler()).accept(csvCellInfo);
                        }
                        cursor++;
                    }
                    else {
                        cursor++;
                    }
                }
                if (CollectionUtils.isNotEmpty(values)) matrix.add(values);
            }
            Object value = shrinkMatrix(matrix, rowCount, columnCount);
            ret.put(metaItem.getKey(), value);
        });
        return ret;
    }
}
