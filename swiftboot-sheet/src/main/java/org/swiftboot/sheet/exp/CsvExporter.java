package org.swiftboot.sheet.exp;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringTokenizer;
import org.swiftboot.sheet.meta.SheetMeta;
import org.swiftboot.sheet.meta.MetaVisitor;
import org.swiftboot.sheet.meta.Position;
import org.swiftboot.util.IoUtils;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Export data into a new or a templated CSV file.
 *
 * @author allen
 */
public class CsvExporter extends BaseExporter {

    public CsvExporter(String fileType) {
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
        List<String> rows;
        if (templateFileStream == null) {
            rows = new ArrayList<>();
        }
        else {
            rows = IoUtils.readToStringList(templateFileStream);
        }
        exportMeta.accept(new MetaVisitor() {
            @Override
            public void visitSingleCell(String key, Position position) {
                extendSheet(rows, position);
                Object value = exportMeta.getValue(key);
                setValueToCell(rows, position, value);
            }

            @Override
            public void visitHorizontalLine(String key, Position startPos, int columnCount) {
                extendSheet(rows, startPos.clone().moveColumns(columnCount));
                List<Object> values = (List<Object>) exportMeta.getValue(key);
                setValuesToRow(rows, startPos, columnCount, values);
            }

            @Override
            public void visitVerticalLine(String key, Position startPos, int rowCount) {
                extendSheet(rows, startPos.clone().moveRows(rowCount));
                List<Object> values = (List<Object>) exportMeta.getValue(key);
                for (int i = 0; i < rowCount; i++) {
                    int iRow = startPos.getRow() + i;
                    String row = rows.get(iRow);
                    if (row != null) {
                        // TODO refactor
                        setValueToCell(rows, new Position(iRow, startPos.getColumn()), values.get(i));
                    }
                }
            }

            @Override
            public void visitMatrix(String key, Position startPos, int rowCount, int columnCount) {
                extendSheet(rows, startPos.clone().moveRows(rowCount));
                extendSheet(rows, startPos.clone().moveColumns(columnCount));
                List<List<Object>> matrix = (List<List<Object>>) exportMeta.getValue(key);
                int actualRowCount = Math.min(rowCount, matrix.size());
                for (int i = 0; i < actualRowCount; i++) {
                    List<Object> values = matrix.get(i);
                    System.out.println(StringUtils.join(values));
                    setValuesToRow(rows, startPos.moveRows(i), columnCount, values);
                }
            }
        });
        BufferedOutputStream bos = new BufferedOutputStream(outputStream);
        for (String row : rows) {
            bos.write(row.getBytes());
            bos.write("\n".getBytes());
        }
        bos.flush();
        bos.close();
    }

    void extendSheet(List<String> lines, Position position) {
        if (lines == null) {
            return;
        }

        // Calculate the original size of a row.
        int originRowSize = 0;
        if (!lines.isEmpty()) {
            StringTokenizer valueTokenizer = StringTokenizer.getCSVInstance(lines.get(0));
            originRowSize = valueTokenizer.size();
        }

        int moreCols = originRowSize - position.getColumn();
        int moreRows = lines.size() - position.getRow();
        // extend columns first
        if (moreCols <= 0) {
            int columnCount = position.getColumn() - originRowSize;
            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                String newLine = new StringBuilder()
                        .append(line)
                        .append(StringUtils.repeat(',', columnCount))
                        .toString();
                lines.set(i, newLine);
            }
        }
        // extend rows
        if (moreRows <= 0) {
            int rowCount = position.getRow() + 1 - lines.size();
            int rowSize = Math.max(originRowSize, position.getColumn() + 1);
            for (int i = 0; i < rowCount; i++) {
                lines.add(StringUtils.repeat(',', rowSize - 1));
            }
        }
    }

    /**
     * Set values from column index of start position, if column count is large than data size,
     *
     * @param rows
     * @param startPos
     * @param columnCount
     * @param values
     */
    void setValuesToRow(List<String> rows, Position startPos, int columnCount, List<Object> values) {
        String row = rows.get(startPos.getRow());
        int from = startPos.getColumn();
        int to = startPos.getColumn() + Math.min(columnCount, values.size());
        StringTokenizer valueTokenizer = StringTokenizer.getCSVInstance(row);
        int cursor = 0;
        List<String> cols = new LinkedList<>();
        while (valueTokenizer.hasNext()) {
            String token = valueTokenizer.next();
            // Include from and exclude to
            if (cursor >= from && cursor < to) {
                Object v = values.get(cursor - from);
                if (v != null) {
                    cols.add(String.valueOf(v));
                }
                else {
                    cols.add(null);
                }
            }
            else {
                cols.add(token);
            }
            cursor += 1;
        }
        rows.set(startPos.getRow(), StringUtils.join(cols, ','));
    }

    private void setValueToCell(List<String> rows, Position position, Object value) {
        setValuesToRow(rows, position, 1, Arrays.asList(new Object[]{value}.clone()));
    }

}
