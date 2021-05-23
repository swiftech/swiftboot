package org.swiftboot.sheet.meta;

/**
 * The visitor to meta.
 */
@FunctionalInterface
public interface MetaVisitor {

    /**
     * Visit a meta item with relative information.
     *
     * @param key
     * @param startPos
     * @param rowCount expect row count
     * @param columnCount expect column count
     * @param value value (for export when needed)
     */
    void visitMetaItem(String key, Position startPos, Integer rowCount, Integer columnCount, Object value,
                       CellHandler<? extends CellInfo> cellHandler);
}
