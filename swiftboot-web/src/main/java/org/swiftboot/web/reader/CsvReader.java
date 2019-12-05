package org.swiftboot.web.reader;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.swiftboot.util.IoUtils;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

/**
 * @author swiftech
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
    public void readCsv(InputStream inputStream, CsvReaderHandler csvHandler) throws Exception {
        List<String> lines = IoUtils.readToStringList(inputStream);
        if (lines.size() < 2) {
            return;
        }
        String titleLine = lines.get(0);
        List<String> titles = Arrays.asList(StringUtils.split(titleLine, ','));
        csvHandler.onTitle(titles);

        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);
            String[] values = StringUtils.split(line, ',');
            csvHandler.onRow(i, Arrays.asList(values));
            for (int j = 0; j < values.length; j++) {
                csvHandler.onCell(j, titles.get(j), values[j]);
            }
            csvHandler.onRowFinished(i);
        }
    }

}
