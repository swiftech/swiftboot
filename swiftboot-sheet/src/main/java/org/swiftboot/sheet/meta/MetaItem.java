package org.swiftboot.sheet.meta;

import org.apache.commons.lang3.ObjectUtils;

/**
 * A meta item represent information about one cell or a group cells in sheet.
 *
 * @author swiftech
 */
public class MetaItem implements Comparable<MetaItem> {

    /**
     * Key to identify value in sheet.
     */
    private String key;

    private Object value;

    private CellHandler<?> cellHandler;

    /**
     * The area to access data in sheet.
     */
    private Area area;

    /**
     * Whether merge cells in area (with data merged and display in center)
     */
    private boolean merge;

    public MetaItem() {
    }

    /**
     * @param key
     * @param area
     */
    public MetaItem(String key, Area area) {
        this.key = key;
        this.area = area;
    }

    /**
     * @param key
     * @param value
     * @param area
     */
    public MetaItem(String key, Object value, Area area) {
        this.key = key;
        this.value = value;
        this.area = area;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public CellHandler<? extends CellInfo> getCellHandler() {
        return cellHandler;
    }

    public void setCellHandler(CellHandler<? extends CellInfo> cellHandler) {
        this.cellHandler = cellHandler;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public boolean isMerge() {
        return merge;
    }

    public void setMerge(boolean merge) {
        this.merge = merge;
    }

    @Override
    public String toString() {
        return "MetaItem{" +
                "key='" + key + '\'' +
                ", value=" + value +
                ", area=" + area +
                '}';
    }

    @Override
    public int compareTo(MetaItem o) {
        return ObjectUtils.compare(this.key, o.getKey());
    }

}
