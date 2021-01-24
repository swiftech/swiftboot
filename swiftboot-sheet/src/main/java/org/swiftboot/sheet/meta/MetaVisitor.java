package org.swiftboot.sheet.meta;

/**
 * The visitor to meta.
 */
public interface MetaVisitor {

    /**
     * Visit a single cell.
     *
     * @param key
     * @param position expect position
     */
    void visitSingleCell(String key, Position position);

    /**
     * Visit a horizontal line of cells.
     *
     * @param key
     * @param startPos
     * @param columnCount   expect column count
     */
    void visitHorizontalLine(String key, Position startPos, Integer columnCount);

    /**
     * Visit a vertical line of cells.
     *
     * @param key
     * @param startPos
     * @param rowCount expect row count
     */
    void visitVerticalLine(String key, Position startPos, Integer rowCount);

    /**
     * Visit a matrix of cells.
     *
     * @param key
     * @param startPos
     * @param rowCount expect row count
     * @param columnCount expect column count
     */
    void visitMatrix(String key, Position startPos, Integer rowCount, Integer columnCount);
}
