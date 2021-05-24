package org.swiftboot.sheet.meta;

/**
 * The visitor to meta.
 */
@FunctionalInterface
public interface MetaVisitor {

    /**
     * Visit a meta item with relative information.
     *
     * @param metaItem
     * @param startPos
     * @param rowCount expect row count
     * @param columnCount expect column count
     */
    void visitMetaItem(MetaItem metaItem, Position startPos, Integer rowCount, Integer columnCount);
}
