package org.swiftboot.sheet.imp;

import org.swiftboot.sheet.meta.SheetMeta;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * Importer to import data from sheet file stream to a Map or an annotated object.
 *
 * @author allen
 */
public interface Importer {

    /**
     * Import data from sheet file stream to an annotated object
     *
     * @param templateFileStream
     * @param resultClass
     * @param <T>
     * @return
     * @throws IOException
     */
    <T> T importFromStream(InputStream templateFileStream, Class<T> resultClass) throws IOException;

    /**
     * Import data from sheet file stream to a Map
     *
     * @param templateFileStream
     * @param meta               Meta list for each parameters to import.
     * @return
     * @throws IOException
     */
    Map<String, Object> importFromStream(InputStream templateFileStream, SheetMeta meta) throws IOException;


}
