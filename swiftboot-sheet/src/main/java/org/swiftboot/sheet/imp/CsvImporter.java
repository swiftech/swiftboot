package org.swiftboot.sheet.imp;

import org.apache.commons.text.StringTokenizer;
import org.swiftboot.sheet.meta.SheetMeta;
import org.swiftboot.util.IoUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Importer for CSV data sheet.
 *
 * @author allen
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
        meta.accept((key, startPos, rowCount, columnCount) -> {
            List<List<Object>> matrix = new ArrayList<>();
            for (int i = 0; i < rowCount; i++) {
                String row = lines.get(startPos.getRow() + i);
                StringTokenizer valueTokenizer = StringTokenizer.getCSVInstance(row);
                List<Object> values = new ArrayList<>();
                int cursor = 0;
                while (valueTokenizer.hasNext()) {
                    String next = valueTokenizer.next();
                    if (cursor >= startPos.getColumn()
                            && cursor < startPos.getColumn() + columnCount) {
                        values.add(next);
                    }
                    cursor += 1;
                }
                matrix.add(values);
            }
            Object value = shrinkMatrix(matrix, rowCount, columnCount);
            ret.put(key, value);
        });
        return ret;
    }
}
