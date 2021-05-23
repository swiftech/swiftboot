package org.swiftboot.sheet.meta;

import org.apache.commons.collections4.ListUtils;

import java.util.*;

/**
 * @author swiftech
 */
public class MetaMap {
    Map<SheetId, List<MetaItem>> data = new LinkedHashMap<>();

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

    public List<MetaItem> sheetItems(SheetId id) {
        return data.get(id);
    }

    public List<MetaItem> allItems() {
        Optional<List<MetaItem>> reduced = data.values().stream().reduce(ListUtils::sum);
        if (reduced.isPresent()) {
            return reduced.get();
        }
        return null;
    }

    public boolean isEmpty() {
        return data.isEmpty();
    }


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
