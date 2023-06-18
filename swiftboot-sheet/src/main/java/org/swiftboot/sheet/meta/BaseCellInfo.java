package org.swiftboot.sheet.meta;

/**
 * @author swiftech
 */
public abstract class BaseCellInfo implements CellInfo {

    /**
     * data of one cell.
     */
    private Object value;

    public BaseCellInfo() {
    }

    public BaseCellInfo(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
