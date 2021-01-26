package org.swiftboot.sheet.exp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author allen
 */
public abstract class BaseExporter implements Exporter {

    /**
     * File type to export.
     */
    private String fileType;

    public BaseExporter(String fileType) {
        this.fileType = fileType;
    }

    /**
     * Convert the value provided by client to a matrix.
     *
     * @param value
     * @param rowCount if = 1, it's a horizontal line
     * @param colCount if = 1, it's a vertical line
     * @return
     */
    protected List<List<Object>> asMatrix(Object value, Integer rowCount, Integer colCount) {
        List<List<Object>> ret = new ArrayList<>();
        boolean isHorizontal = rowCount != null && rowCount == 1;
        boolean isVertical = colCount != null && colCount == 1;
        if (value instanceof List) {
            List<Object> newRow = new ArrayList<>();// 行需要特殊处理
            for (Object subValue : ((List<?>) value)) {
                if (subValue instanceof List) {
                    List<Object> e = (List<Object>) subValue;
                    ret.add(e);
                }
                else {
                    if (isHorizontal) {
                        newRow.add(subValue); // collect for a new line for horizontal
                    }
                    else if (isVertical) {
                        ret.add(Collections.singletonList(subValue)); // add single value as new line for vertical
                    }
                }
            }
            if (isHorizontal) {
                ret.add(newRow);
            }
        }
        else {
            ret.add(Collections.singletonList(value));
        }
        return ret;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

}
