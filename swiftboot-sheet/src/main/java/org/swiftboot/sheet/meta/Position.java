package org.swiftboot.sheet.meta;

import org.swiftboot.sheet.util.CalculateUtils;
import org.swiftboot.sheet.util.IndexUtils;

import java.util.Objects;
import java.util.function.BiFunction;

/**
 * Position in sheet
 *
 * @author swiftech
 */
public class Position {

    /**
     * Row index starts from 0
     */
    Integer row;

    /**
     * Columns index starts from 0
     */
    Integer column;

    public Position(Integer row, Integer column) {
        if (!IndexUtils.isLegalRow(row) || !IndexUtils.isLegalColumn(column)) {
            throw new RuntimeException("Row or column index is illegal for position");
        }
        this.row = row;
        this.column = column;
    }

    @Override
    public Position clone() {
        return new Position(row, column);
    }

    /**
     * Add more rows to row index
     *
     * @param rows
     * @return
     */
    public Position moveRows(Integer rows) {
        if (!IndexUtils.isLegalRow(row)) {
            throw new RuntimeException("Row index can is illegal for position");
        }
        if (rows != null) {
            this.row += rows;
        }
        return this;
    }

    /**
     * Add more columns to column index.
     *
     * @param columns
     * @return
     */
    public Position moveColumns(Integer columns) {
        if (!IndexUtils.isLegalColumn(column)) {
            throw new RuntimeException("Column index can is illegal for position");
        }
        if (columns != null) {
            this.column += columns;
        }
        return this;
    }

    public boolean isUncertain() {
        return this.row == null || this.column == null;
    }

    /**
     * Create a enlarged position by 2 positions, uncertain row or column index will be ignored.
     *
     * @param p1
     * @param p2
     * @return
     */
    public static Position enlarge(Position p1, Position p2) {
        return overlay(p1, p2, CalculateUtils::max);
    }

    /**
     * Create a narrowed position by 2 positions, uncertain row or column index will be ignored.
     *
     * @param p1
     * @param p2
     * @return
     */
    public static Position narrow(Position p1, Position p2) {
        return overlay(p1, p2, CalculateUtils::min);
    }


    /**
     * overlay 2 positions and return new position by 'function'
     *
     * @param p1
     * @param p2
     * @param function
     * @return
     */
    static Position overlay(Position p1, Position p2, BiFunction<Integer, Integer, Integer> function) {
        if (p1 == null && p2 == null) {
            return null;
        }
        Integer maxRow = p1 == null ? p2.getRow() : (p2 == null ? p1.getRow() : function.apply(p1.getRow(), p2.getRow()));
        Integer maxCol = p1 == null ? p2.getColumn() : (p2 == null ? p1.getColumn() : function.apply(p1.getColumn(), p2.getColumn()));
        return new Position(maxRow, maxCol);
    }


    public Integer getRow() {
        return row;
    }

    public void setRow(Integer row) {
        this.row = row;
    }

    public Integer getColumn() {
        return column;
    }

    public void setColumn(Integer column) {
        this.column = column;
    }

    @Override
    public String toString() {
        return "Position(" + row +
                ", " + column +
                ')';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return Objects.equals(row, position.row) && Objects.equals(column, position.column);
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, column);
    }


}
