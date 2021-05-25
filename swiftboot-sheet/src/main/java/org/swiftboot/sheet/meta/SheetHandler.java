package org.swiftboot.sheet.meta;

/**
 * @author swiftech
 * @param <T>
 */
@FunctionalInterface
public interface SheetHandler<T extends SheetInfo> {

    void onSheet(T sheetInfo);
}
