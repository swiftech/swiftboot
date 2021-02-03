package org.swiftboot.sheet.meta;

import java.util.Objects;

/**
 * @author allen
 */
public class Area {

    /**
     * Start position of this area, this cannot be null.
     */
    Position startPosition;

    /**
     * End position of this area, inclusive.
     * this can be null which means uncertain rows and columns of this area.
     */
    Position endPosition;

    public static Area newHorizontal(Position startPosition, int length) {
        return new Area(startPosition, new Position(startPosition.getRow(), startPosition.getColumn() + length));
    }

    public static Area newVertical(Position startPosition, int length) {
        return new Area(startPosition, new Position(startPosition.getRow() + length, startPosition.getColumn()));
    }

    public Area(Position startPosition) {
        this.startPosition = startPosition;
    }

    public Area(Position startPosition, Position endPosition) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
    }

    public Area(Integer row1, Integer column1, Integer row2, Integer column2) {
        this.startPosition = new Position(row1, column1);
        this.endPosition = new Position(row2, column2);
    }

    /**
     * Row count from start position to end position
     *
     * @return
     */
    public Integer rowCount() {
        if (startPosition == null) {
            return 0;
        }
        if (isSingleCell()) {
            return 1;
        }
        if (endPosition.row == null) {
            return null; // must be uncertain size
        }
        return Math.abs(endPosition.row - startPosition.row) + 1;
    }

    /**
     * Column count from start position to end position.
     *
     * @return
     */
    public Integer columnCount() {
        if (startPosition == null) {
            return 0;
        }
        if (isSingleCell()) {
            return 1;
        }
        if (endPosition.column == null) {
            return null; // must be uncertain size
        }
        return Math.abs(endPosition.column - startPosition.column) + 1;
    }

    public int size() {
        if (startPosition == null) {
            return 0;
        }
        if (isSingleCell()) {
            return 1;
        }
        return rowCount() * columnCount();
    }

    public boolean isSingleCell() {
        return startPosition != null && (endPosition == null || startPosition.equals(endPosition));
    }

    public boolean isLine() {
        return startPosition.getRow().equals(endPosition.getRow())
                || startPosition.getColumn().equals(endPosition.getColumn());
    }

    /**
     * Calculate overlay, uncertain row or column index will be ignored.
     *
     * @param area
     */
    public Area overlay(Area area) {
        Position nearest = Position.narrow(this.startPosition, area.getStartPosition());
        // Enlarge with start position if no end position
        Position farthest = Position.enlarge(
                this.endPosition == null ? this.startPosition : this.endPosition,
                area.getEndPosition() == null ? area.getStartPosition() : area.getEndPosition());
        return new Area(nearest, farthest);
    }

    public Position getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(Position startPosition) {
        this.startPosition = startPosition;
    }

    public Position getEndPosition() {
        return endPosition;
    }

    public void setEndPosition(Position endPosition) {
        this.endPosition = endPosition;
    }

    @Override
    public String toString() {
        return "Area{" +
                "startPosition=" + startPosition +
                ", endPosition=" + endPosition +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Area area = (Area) o;
        return Objects.equals(startPosition, area.startPosition) && Objects.equals(endPosition, area.endPosition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startPosition, endPosition);
    }
}
