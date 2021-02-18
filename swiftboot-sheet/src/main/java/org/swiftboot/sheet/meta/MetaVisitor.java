package org.swiftboot.sheet.meta;

/**
 * The visitor to meta.
 */
public interface MetaVisitor {

    /**
     * Visit a meta item with relative information.
     *
     * @param key
     * @param startPos
     * @param rowCount expect row count
     * @param columnCount expect column count
     */
    void visitMetaItem(String key, Position startPos, Integer rowCount, Integer columnCount);
}
