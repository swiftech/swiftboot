package org.swiftboot.sheet.excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.swiftboot.sheet.meta.BaseCellInfo;

/**
 * Cell info for MS-Excel
 *
 * @author swiftech
 */
public class ExcelCellInfo extends BaseCellInfo {
    /**
     * The workbook object that the cell object belongs
     */
    private Workbook workbook;
    /**
     * The sheet object that the cell object belongs
     */
    private Sheet sheet;
    /**
     * The target cell object.
     */
    private Cell cell;
    /**
     * Row index of this cell in an area, NOT in sheet.
     */
    private int rowIdx;
    /**
     * Column index of this cell in an area, NOT in sheet.
     */
    private int colIdx;

    public ExcelCellInfo() {
    }

    public ExcelCellInfo(Workbook workbook, Sheet sheet, Cell cell, int rowIdx, int colIdx) {
        this.workbook = workbook;
        this.sheet = sheet;
        this.cell = cell;
        this.rowIdx = rowIdx;
        this.colIdx = colIdx;
    }

    public Workbook getWorkbook() {
        return workbook;
    }

    public void setWorkbook(Workbook workbook) {
        this.workbook = workbook;
    }

    public Sheet getSheet() {
        return sheet;
    }

    public void setSheet(Sheet sheet) {
        this.sheet = sheet;
    }

    public Cell getCell() {
        return cell;
    }

    public void setCell(Cell cell) {
        this.cell = cell;
    }

    public int getRowIdx() {
        return rowIdx;
    }

    public void setRowIdx(int rowIdx) {
        this.rowIdx = rowIdx;
    }

    public int getColIdx() {
        return colIdx;
    }

    public void setColIdx(int colIdx) {
        this.colIdx = colIdx;
    }
}
