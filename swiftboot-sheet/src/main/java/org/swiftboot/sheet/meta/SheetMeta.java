package org.swiftboot.sheet.meta;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Meta for accessing sheet data.
 * TODO
 * Use method addItem() to define relations between keys of data and areas in sheet directly.
 * Use method fromExpression() to define the relations if you want to indicate areas by expression.
 *
 * @author allen
 */
public class SheetMeta {

    private final Logger log = LoggerFactory.getLogger(SheetMeta.class);

    /**
     * relation: sheet id -> list of meta item(key, export value and area in sheet)
     */
    private MetaMap metaMap = new MetaMap();

    /**
     * If not allowed, any areas with uncertain end position will let exception threw.
     */
    private boolean isAllowFreeSize = false;

    public SheetMeta() {
    }

    public SheetMeta(MetaMap metaMap) {
        this.metaMap = metaMap;
    }


    public List<MetaItem> getMetaItems(int sheetIndex) {
        return metaMap.sheetItems(new SheetId(sheetIndex));
    }

    public List<MetaItem> getMetaItems(String sheetName) {
        return metaMap.sheetItems(new SheetId(sheetName));
    }

    public MetaMap getMetaMap() {
        return metaMap;
    }

    public List<MetaItem> getAllMetaItems() {
        return metaMap.data.values().stream().flatMap((Function<List<MetaItem>, Stream<MetaItem>>) Collection::stream).collect(Collectors.toList());
    }

    /**
     * Accept visitor to visit meta items.
     *
     * @param visitor
     */
    public void accept(MetaVisitor visitor) {
        if (this.metaMap == null || this.metaMap.isEmpty()) {
            throw new RuntimeException("Setup meta first");
        }

        for (SheetId sheetId : metaMap.data.keySet()) {
            List<MetaItem> items = metaMap.sheetItems(sheetId);
            for (MetaItem meta : items) {
                Area area = meta.getArea();
                Position startingPos = area.getStartPosition();
                log.debug(String.format("'%s' -> %s%n", meta.getKey(), meta.getArea()));
                if (area.isSingleCell()) {
                    visitor.visitMetaItem(meta.getKey(), startingPos, 1, 1, meta.getValue());
                }
                else {
                    Integer rowCount = area.rowCount();
                    Integer columnCount = area.columnCount();
                    log.debug(String.format("row count %d, column count %d%n", rowCount, columnCount));
                    if (!isAllowFreeSize && (rowCount == null || columnCount == null)) {
                        throw new RuntimeException("Free size expression is not allowed");
                    }
                    else if (area.isLine()) {
                        // Only one row is horizontal
                        if (rowCount != null && rowCount == 1) {
                            visitor.visitMetaItem(meta.getKey(), startingPos, 1, columnCount, meta.getValue());
                        }
                        else if (columnCount != null && columnCount == 1) { // Only one column is vertical
                            visitor.visitMetaItem(meta.getKey(), startingPos, rowCount, 1, meta.getValue());
                        }
                    }
                    else {
                        visitor.visitMetaItem(meta.getKey(), startingPos, rowCount, columnCount, meta.getValue());
                    }
                }
            }

        }
    }


    /**
     * Utils method that find max (end) position from all areas.
     *
     * @return not null
     */
    public Position findMaxPosition() {
        Area overlayedArea = new Area(0, 0, 0, 0);
        List<MetaItem> metaItems = this.getAllMetaItems();
        for (MetaItem metaItem : metaItems) {
            overlayedArea = overlayedArea.overlay(metaItem.getArea());
        }
        log.debug("Overlayed area: " + overlayedArea);
        return overlayedArea.getEndPosition();
    }

    public boolean isAllowFreeSize() {
        return isAllowFreeSize;
    }

    public void setAllowFreeSize(boolean allowFreeSize) {
        isAllowFreeSize = allowFreeSize;
    }


    public static class MetaMap {
        Map<SheetId, List<MetaItem>> data = new HashMap<>();

        public void addItem(SheetId id, MetaItem item) {
            List<MetaItem> sub = data.computeIfAbsent(id, k -> new ArrayList<>());
            sub.add(item);
        }

        public List<MetaItem> sheetItems(SheetId id) {
            return data.get(id);
        }

        public boolean isEmpty() {
            return data.isEmpty();
        }

    }

}
