package org.swiftboot.sheet.util;

/**
 * @author swiftech
 */
public class IndexUtils {

    /**
     * Null or 0~65535 is legal index of row.
     *
     * @param rowIdx
     * @return
     */
    public static boolean isLegalRow(Integer rowIdx) {
        return rowIdx == null || (rowIdx >= 0 && rowIdx < 65536);
    }

    /**
     * Null or 0~255 is legal index of column
     * @param colIdx
     * @return
     */
    public static boolean isLegalColumn(Integer colIdx){
        return colIdx == null || (colIdx >= 0 && colIdx < 256);
    }

}
