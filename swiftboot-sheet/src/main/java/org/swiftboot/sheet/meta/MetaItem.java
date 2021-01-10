package org.swiftboot.sheet.meta;

/**
 * A meta item represent information about one cell or a group cells in sheet.
 *
 * @author allen
 */
public class MetaItem {

    /**
     * Key to identify value in sheet.
     */
    private String key;

    /**
     * Index of sheet, default is 0.
     */
    private int sheetIndex = 0;

    /**
     * The area to access data in sheet.
     */
    private Area area;

    /**
     *
     * @param key
     * @param sheetIndex
     * @param area
     */
    public MetaItem(String key, int sheetIndex, Area area) {
        this.key = key;
        this.sheetIndex = sheetIndex;
        this.area = area;
    }

    /**
     *
     *
     * @param key
     * @param area
     */
    public MetaItem(String key, Area area) {
        this.key = key;
        this.area = area;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getSheetIndex() {
        return sheetIndex;
    }

    public void setSheetIndex(int sheetIndex) {
        this.sheetIndex = sheetIndex;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    @Override
    public String toString() {
        return "Meta{" +
                "key='" + key + '\'' +
                ", sheetIndex=" + sheetIndex +
                ", area=" + area +
                '}';
    }
}
