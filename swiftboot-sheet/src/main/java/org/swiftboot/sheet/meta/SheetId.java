package org.swiftboot.sheet.meta;

import java.util.Objects;

/**
 * Represent a sheet in the workbook.
 * Either sheetIndex or sheetName are identical,
 *
 * @author allen
 */
public class SheetId {

    public static final SheetId DEFAULT_SHEET = new SheetId(0, "Sheet 1");

    /**
     * Sheet index, 0 is the first sheet in the workbook.
     */
    Integer sheetIndex;

    /**
     * Sheet name
     */
    String sheetName;

    public SheetId() {
    }

    public SheetId(Integer sheetIndex) {
        this.sheetIndex = sheetIndex;
    }

    public SheetId(String sheetName) {
        this.sheetName = sheetName;
    }

    public SheetId(Integer sheetIndex, String sheetName) {
        this.sheetIndex = sheetIndex;
        this.sheetName = sheetName;
    }

    public int getSheetIndex() {
        return sheetIndex;
    }

    public void setSheetIndex(int sheetIndex) {
        this.sheetIndex = sheetIndex;
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    @Override
    public String toString() {
        return "SheetId{" +
                "sheetIndex=" + sheetIndex +
                ", sheetName='" + sheetName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SheetId sheetId = (SheetId) o;
        return Objects.equals(sheetIndex, sheetId.sheetIndex) || Objects.equals(sheetName, sheetId.sheetName);
    }

    @Override
    public int hashCode() {
        if (sheetIndex != null) {
            return Objects.hash(sheetIndex);
        }
        else {
            return Objects.hashCode(sheetName);
        }
    }
}