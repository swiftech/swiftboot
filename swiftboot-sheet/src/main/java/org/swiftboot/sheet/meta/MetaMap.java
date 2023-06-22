package org.swiftboot.sheet.meta;

import org.apache.commons.collections4.ListUtils;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author swiftech
 */
public class MetaMap {
    /**
     * mapping from sheet id to meta items in the order added.
     */
    Map<SheetId, List<MetaItem>> data = new LinkedHashMap<>();

    Map<SheetId, Consumer<? extends SheetInfo>> handlers = new HashMap<>();

    /**
     * to indicate that extract images during parsing Excel sheet, for import only.
     */
    boolean withImages = false;
    Map<SheetId, Function<Picture, ?>> imageConverters = new HashMap<>();

    public void setSheetHandler(SheetId id, Consumer<? extends SheetInfo> handler) {
        handlers.put(id, handler);
    }

    public Consumer<? extends SheetInfo> getSheetHandler(SheetId id) {
        return handlers.get(id);
    }

    public boolean isWithImages() {
        return withImages;
    }

    public void setWithImages(boolean withImages) {
        this.withImages = withImages;
    }

    public void setImageConverter(SheetId id,Function<Picture, ?> converter) {
        this.imageConverters.put(id, converter);
    }

    public Function<Picture, ?> getImageConverter(SheetId id) {
        return this.imageConverters.get(id);
    }

    /**
     * Add item to a sheet.
     *
     * @param id
     * @param item
     */
    public void addItem(SheetId id, MetaItem item) {
        List<MetaItem> sub = data.computeIfAbsent(id, k -> new ArrayList<>());
        sub.add(item);
    }

    /**
     * Get all items for a sheet.
     *
     * @param id
     * @return
     */
    public List<MetaItem> sheetItems(SheetId id) {
        return data.get(id);
    }

    /**
     * Get all items.
     *
     * @return
     */
    public List<MetaItem> allItems() {
        Optional<List<MetaItem>> reduced = data.values().stream().reduce(ListUtils::sum);
        return reduced.orElse(null);
    }

    public boolean isEmpty() {
        return data.isEmpty();
    }

    /**
     * Traverse all sheets
     *
     * @param onSheet
     */
    public void traverse(OnSheet onSheet) {
        for (SheetId sheetId : data.keySet()) {
            onSheet.onSheet(sheetId, data.get(sheetId));
        }
    }

    @FunctionalInterface
    public interface OnSheet {
        void onSheet(SheetId sheetId, List<MetaItem> items);
    }
}
