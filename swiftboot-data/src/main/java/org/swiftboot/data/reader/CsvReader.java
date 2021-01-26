package org.swiftboot.data.reader;

import org.apache.commons.text.StringTokenizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.swiftboot.util.IoUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

/**
 * @author swiftech
 * @since 1.1
 */
public class CsvReader {


    private Logger log = LoggerFactory.getLogger(CsvReader.class);

    /**
     * 逐行读取 CSV 格式的输入流，并通过 CsvReaderHandler 回调
     *
     * @param inputStream
     * @param csvHandler
     * @throws Exception
     */
    public void readCsv(InputStream inputStream, CsvReaderHandler csvHandler) throws IOException {
        List<String> lines = IoUtils.readToStringList(inputStream);
        inputStream.close();
        if (lines.size() < 2) {
            return;
        }
        String titleLine = lines.get(0);

        StringTokenizer titleTokenizer = StringTokenizer.getCSVInstance(titleLine);
        List<String> titles = new LinkedList<>();
        while (titleTokenizer.hasNext()) {
            titles.add(titleTokenizer.nextToken());
        }
        csvHandler.onTitle(titles);

        // 从行 1 开始的数据行
        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);
            StringTokenizer valueTokenizer = StringTokenizer.getCSVInstance(line);
            List<String> values = new LinkedList<>();
            while (valueTokenizer.hasNext()) {
                values.add(valueTokenizer.nextToken());
            }
            if (values.size() != titles.size()) {
                throw new RuntimeException(String.format("Row %d has different numbers of columns(%d) to titles(%d)", i, values.size(), titles.size()));
            }
            csvHandler.onRow(i, values);
            for (int j = 0; j < values.size(); j++) {
                csvHandler.onCell(j, titles.get(j), values.get(j));
            }
            csvHandler.onRowFinished(i);
        }

    }


}
