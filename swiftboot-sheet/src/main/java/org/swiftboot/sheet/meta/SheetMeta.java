package org.swiftboot.sheet.meta;

import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Meta for accessing sheet data.
 *
 * @author swiftech
 * @see SheetMetaBuilder
 */
public class SheetMeta {

    private static final Logger log = LoggerFactory.getLogger(SheetMeta.class);

    /**
     * relation: sheet id -> list of meta item(key, export value and area in sheet)
     */
    private MetaMap metaMap = new MetaMap();

    /**
     * If not allowed, any areas with uncertain end position will raise exception.
     * Basically, it is Tree for export but False for import.
     */
    private boolean isAllowFreeSize = false;

    public SheetMeta() {
    }

    public SheetMeta(MetaMap metaMap) {
        this.metaMap = metaMap;
    }

    public Consumer<? extends SheetInfo> getSheetHandler(SheetId sheetId) {
        return this.metaMap.getSheetHandler(sheetId);
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
        this.accept(null, visitor);
    }

    /**
     * Accept visitor to visit sheet and meta items.
     *
     * @param sheetVisitor
     * @param itemVisitor
     */
    public void accept(SheetVisitor sheetVisitor, MetaVisitor itemVisitor) {
        if (this.metaMap == null || this.metaMap.isEmpty()) {
            throw new RuntimeException("Setup meta first");
        }

        for (SheetId sheetId : metaMap.data.keySet()) {
            if (sheetVisitor != null) {
                sheetVisitor.visitSheet(sheetId);
            }
            List<MetaItem> items = metaMap.sheetItems(sheetId);
            for (MetaItem meta : items) {
                Area area = meta.getArea();
                if (area != null) {
                    Position startingPos = ObjectUtils.firstNonNull(area.getStartPosition(), Position.ZERO);
                    log.debug(String.format("'%s' -> %s", meta.getKey(), meta.getArea()));
                    if (area.isSingleCell()) {
                        itemVisitor.visitMetaItem(meta, startingPos, 1, 1);
                    }
                    else {
                        Integer rowCount = area.rowCount();
                        Integer columnCount = area.columnCount();
                        log.debug(String.format("row count %s, column count %d", ObjectUtils.firstNonNull(rowCount, "is uncertain"), columnCount));
                        if (!isAllowFreeSize && (rowCount == null || columnCount == null)) {
                            throw new RuntimeException("Free size expression is not allowed");
                        }
                        else if (area.isLine()) {
                            // Only one row is horizontal
                            if (rowCount != null && rowCount == 1) {
                                itemVisitor.visitMetaItem(meta, startingPos, 1, columnCount);
                            }
                            else if (columnCount != null && columnCount == 1) { // Only one column is vertical
                                itemVisitor.visitMetaItem(meta, startingPos, rowCount, 1);
                            }
                        }
                        else {
                            itemVisitor.visitMetaItem(meta, startingPos, rowCount, columnCount);
                        }
                    }
                }
            }
        }
    }


    /**
     * Utils method that find max (end) position from all areas in a sheet.
     *
     * @param sheetId
     * @return not null
     */
    public Position findMaxPosition(SheetId sheetId) {
        Area overlayedArea = new Area(0, 0, 0, 0);
        List<MetaItem> metaItems = this.metaMap.sheetItems(sheetId);
        for (MetaItem metaItem : metaItems) {
            overlayedArea = overlayedArea.overlay(metaItem.getArea());
        }
        log.debug("Overlayed area: " + overlayedArea);
        return overlayedArea.getEndPosition();
    }

    /**
     * Utils method that find max (end) position from all areas in all sheet.
     *
     * @return
     */
    public Position findMaxPosition() {
        Area overlayedArea = new Area(0, 0, 0, 0);
        List<MetaItem> metaItems = this.metaMap.allItems();
        for (MetaItem metaItem : metaItems) {
            overlayedArea = overlayedArea.overlay(metaItem.getArea());
        }
        log.debug("Overlayed area: " + overlayedArea);
        return overlayedArea.getEndPosition();
    }

    public void print() {
        metaMap.traverse((sheetId, items) -> {
            System.out.println(sheetId);
            for (MetaItem item : items) {
                System.out.println(item);
            }
        });
    }

    public boolean isAllowFreeSize() {
        return isAllowFreeSize;
    }

    public void setAllowFreeSize(boolean allowFreeSize) {
        isAllowFreeSize = allowFreeSize;
    }

}
