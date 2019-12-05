package org.swiftboot.web.reader;

import java.util.List;

/**
 * @author swiftech
 */
public interface CsvReaderHandler {

    /**
     * 读取到 CSV 表格头
     *
     * @param titles
     */
    void onTitle(List<String> titles);

    /**
     * 读取到一行
     *
     * @param rowNum  行号
     * @param columns 一行的内容
     */
    void onRow(int rowNum, List<String> columns);

    /**
     * 读到一个单元格
     *
     * @param colNum
     * @param columnName
     * @param cellValue
     */
    void onCell(int colNum, String columnName, String cellValue);

    /**
     * 完成 CSV 文件的读取
     *
     * @param rowNow 最后一行的行号
     */
    void onRowFinished(int rowNow);
}
