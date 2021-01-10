package org.swiftboot.sheet.meta;

import java.util.Objects;

/**
 * Position in sheet
 *
 * @author allen
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
        this.row = row;
        this.column = column;
    }

    @Override
    public Position clone() {
        return new Position(row, column);
    }

    public Position moveRows(int rows) {
        this.row += rows;
        return this;
    }

    public Position moveColumns(int columns) {
        this.column += columns;
        return this;
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
        return "Position{" +
                "row=" + row +
                ", column=" + column +
                '}';
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
