package org.swiftboot.sheet.meta;

/**
 * The visitor for sheet id.
 *
 * @author swiftech
 */
@FunctionalInterface
public interface SheetVisitor {

    void visitSheet(SheetId sheetId);
}
