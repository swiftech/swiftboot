package org.swiftboot.sheet.imp;

import org.apache.commons.text.StringTokenizer;
import org.swiftboot.sheet.meta.MetaVisitor;
import org.swiftboot.sheet.meta.Position;
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
        meta.accept(new MetaVisitor() {
            @Override
            public void visitSingleCell(String key, Position position) {
                String row = lines.get(position.getRow());
                StringTokenizer valueTokenizer = StringTokenizer.getCSVInstance(row);
                int cursor = 0;
                while (valueTokenizer.hasNext()) {
                    String token = valueTokenizer.next();
                    if (cursor == position.getColumn()) {
                        ret.put(key, token);
                        break;
                    }
                    cursor += 1;
                }
            }

            @Override
            public void visitHorizontalLine(String key, Position startPos, int columnCount) {
                String row = lines.get(startPos.getRow());
                StringTokenizer valueTokenizer = StringTokenizer.getCSVInstance(row);
                List<String> values = new ArrayList<>();
                int cursor = 0;
                while (valueTokenizer.hasNext()) {
                    String next = valueTokenizer.next();
                    if (cursor >= startPos.getColumn() && cursor < startPos.getColumn() + columnCount) {
                        values.add(next);
                    }
                    cursor += 1;
                }
                ret.put(key, values);
            }

            @Override
            public void visitVerticalLine(String key, Position startPos, int rowCount) {
                List<String> values = new ArrayList<>();
                for (int i = 0; i < rowCount; i++) {
                    String row = lines.get(startPos.getRow() + i);
                    StringTokenizer valueTokenizer = StringTokenizer.getCSVInstance(row);
                    int cursor = 0;
                    while (valueTokenizer.hasNext()) {
                        String next = valueTokenizer.next();
                        if (cursor == startPos.getColumn()) {
                            values.add(next);
                            break;
                        }
                        cursor += 1;
                    }
                }
                ret.put(key, values);
            }

            @Override
            public void visitMatrix(String key, Position startPos, int rowCount, int columnCount) {
                List<List<String>> matrix = new ArrayList<>();
                for (int i = 0; i < rowCount; i++) {
                    String row = lines.get(startPos.getRow() + i);
                    StringTokenizer valueTokenizer = StringTokenizer.getCSVInstance(row);
                    List<String> values = new ArrayList<>();
                    int cursor = 0;
                    while (valueTokenizer.hasNext()) {
                        String next = valueTokenizer.next();
                        if (cursor >= startPos.getColumn() && cursor < startPos.getColumn() + columnCount) {
                            values.add(next);
                        }
                        cursor += 1;
                    }
                    matrix.add(values);
                }
                ret.put(key, matrix);
            }
        });
        return ret;
    }
}
