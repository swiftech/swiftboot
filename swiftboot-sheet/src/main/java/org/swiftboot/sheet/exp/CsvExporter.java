package org.swiftboot.sheet.exp;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringTokenizer;
import org.swiftboot.sheet.meta.*;
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
 * Note:
 * multiple sheets and pictures export are not supported,
 * if pictures provides, it will be ignored.
 * if multiple sheet id provides, all items for these sheet ids will be exported to one CSV file.
 *
 * @author swiftech
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
        List<String> rows;
        if (templateFileStream == null) {
            rows = new ArrayList<>();
        }
        else {
            rows = IoUtils.readToStringList(templateFileStream);
        }
        this.extendSheet(rows, exportMeta.findMaxPosition());
        exportMeta.setAllowFreeSize(true);
        exportMeta.accept((metaItem, startPos, rowCount, columnCount) -> {
            if (metaItem.getValue() == null) {
                throw new RuntimeException(String.format("No value provided to export for key: %s", metaItem.getKey()));
            }
            if (metaItem.getValue() instanceof PictureLoader) {
                log.warn("Picture is not supported to export to CSV, just ignore ");
            }
            else {
                List<List<Object>> matrix = asMatrix(metaItem.getValue(), rowCount, columnCount);
                if (matrix.isEmpty()){
                    return;
                }
                int actualRowCount = rowCount == null ? matrix.size() : Math.min(rowCount, matrix.size());
                int actualColumnCount = columnCount == null ? matrix.get(0).size() : Math.min(columnCount, matrix.get(0).size());
                for (int i = 0; i < actualRowCount; i++) {
                    List<Object> values = matrix.get(i);
                    log.debug(StringUtils.join(values));
                    setValuesToRow(rows, startPos.clone().moveRows(i), actualColumnCount, values);
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

    /**
     * Extends sheet
     *
     * @param lines    String lines that represents a sheet
     * @param position end position this sheet should extends to
     */
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

        int moreCols = position.getColumn() + 1 - originRowSize;
        // extend columns first
        if (moreCols > 0) {
            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                String newLine = new StringBuilder()
                        .append(line)
                        .append(StringUtils.repeat(',', moreCols))
                        .toString();
                lines.set(i, newLine);
            }
        }
        // extend rows
        int moreRows = position.getRow() + 1 - lines.size();
        if (moreRows > 0) {
            int rowSize = Math.max(originRowSize, position.getColumn() + 1);
            for (int i = 0; i < moreRows; i++) {
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
