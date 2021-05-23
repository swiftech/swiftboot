package org.swiftboot.sheet.meta;

/**
 * Handle a sheet cell directly for user client.
 *
 * @param <T> Type of information object on callback for a cell.
 * @author swiftech
 */
@FunctionalInterface
public interface CellHandler<T extends CellInfo> {

    /**
     * Called when a cell is visited, after the cell value is read or written.
     *
     * @param cellInfo
     */
    void onCell(T cellInfo);
}
