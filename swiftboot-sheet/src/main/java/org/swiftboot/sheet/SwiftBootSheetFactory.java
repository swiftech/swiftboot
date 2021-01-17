package org.swiftboot.sheet;

import org.swiftboot.sheet.exp.CsvExporter;
import org.swiftboot.sheet.exp.ExcelExporter;
import org.swiftboot.sheet.exp.Exporter;
import org.swiftboot.sheet.imp.CsvImporter;
import org.swiftboot.sheet.imp.ExcelImporter;
import org.swiftboot.sheet.imp.Importer;

import static org.swiftboot.sheet.constant.SheetFileType.*;

/**
 * @author allen
 */
public class SwiftBootSheetFactory {

    /**
     * Create a importer by file suffix.
     *
     * @param suffix
     * @return
     */
    public Importer createImporter(String suffix) {
        Importer importer;
        if (TYPE_XLSX.equals(suffix) || TYPE_XLS.equals(suffix)) {
            importer = new ExcelImporter(suffix);
        }
        else if (TYPE_CSV.equals(suffix)) {
            importer = new CsvImporter(TYPE_CSV);
        }
        else {
            throw new RuntimeException("Not supported file type: " + suffix);
        }
        return importer;
    }

    /**
     * Create a exporter by file suffix.
     *
     * @param suffix
     * @return
     */
    public Exporter createExporter(String suffix) {
        Exporter exporter = null;
        if (TYPE_XLSX.equals(suffix) || TYPE_XLS.equals(suffix)) {
            exporter = new ExcelExporter(suffix);
        }
        else if (TYPE_CSV.equals(suffix)) {
            exporter = new CsvExporter(TYPE_CSV);
        }
        else {
            throw new RuntimeException("Not supported file type: " + suffix);
        }
        return exporter;
    }
}
