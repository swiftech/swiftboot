package org.swiftboot.web.reader;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

/**
 * @author swiftech
 */
public class CsvReaderTest {

    public static final char QUOTE = '"';

    private StringBuilder CSV_DATA = new StringBuilder();

    @BeforeEach
    public void setup() {
        // Title
        CSV_DATA.append(QUOTE).append("A").append(QUOTE)
                .append(",").append(QUOTE).append("B\"\"B").append(QUOTE)
                .append(",").append(QUOTE).append("C,C").append(QUOTE)
                .append(",").append(QUOTE).append("D\"\"D,D").append(QUOTE);

        CSV_DATA.append("\n");
        // Data
        CSV_DATA.append(QUOTE).append("a").append(QUOTE)
                .append(",").append(QUOTE).append("b\"\"b").append(QUOTE)
                .append(",").append(QUOTE).append("c,c").append(QUOTE)
                .append(",").append(QUOTE).append("d\"\"d,d").append(QUOTE);

    }

    @Test
    public void test() throws IOException {
        System.out.println("Parse CSV: \n" + CSV_DATA.toString());
        CsvReader visitor= new CsvReader();
        visitor.readCsv(new ByteArrayInputStream(CSV_DATA.toString().getBytes()), new CsvReaderHandler() {
            @Override
            public void onTitle(List<String> titles) {
                System.out.println("Title: " + StringUtils.join(titles, " | "));
            }

            @Override
            public void onRow(int rowNum, List<String> columns) {
                System.out.println("Row: " + StringUtils.join(columns, " | "));
            }

            @Override
            public void onCell(int colNum, String columnName, String cellValue) {
            }

            @Override
            public void onRowFinished(int rowNow) {
                System.out.println("Ends with row: " +rowNow);
            }
        });
        System.out.println("----");
    }

}
