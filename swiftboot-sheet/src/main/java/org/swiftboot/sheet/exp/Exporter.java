package org.swiftboot.sheet.exp;

import org.swiftboot.sheet.meta.SheetMeta;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Exporter to export data from an annotated object or a Map in {@link SheetMeta} to a sheet file stream.
 *
 * @author allen
 */
public interface Exporter {

    /**
     * Export data from an annotated object to sheet file stream.
     *
     * @param dataObject
     * @param outputStream
     * @param <T>
     * @throws IOException
     */
    <T> void export(Object dataObject, OutputStream outputStream) throws IOException;

    /**
     * Export data from an annotated object to sheet file stream which with everything from template sheet file.
     *
     * @param templateFileStream
     * @param dataObject
     * @param outputStream
     * @param <T>
     * @throws IOException
     */
    <T> void export(InputStream templateFileStream, Object dataObject, OutputStream outputStream) throws IOException;

    /**
     * Export data from a map in {@link SheetMeta} to sheet file stream.
     *
     * @param exportMeta
     * @param outputStream
     * @throws IOException
     */
    void export(SheetMeta exportMeta, OutputStream outputStream) throws IOException;

    /**
     * Export data from a map in {@link SheetMeta} to sheet file stream which with everything from template sheet file.
     *
     * @param templateFileStream
     * @param exportMeta
     * @param outputStream
     * @throws IOException
     */
    void export(InputStream templateFileStream, SheetMeta exportMeta, OutputStream outputStream) throws IOException;
}
