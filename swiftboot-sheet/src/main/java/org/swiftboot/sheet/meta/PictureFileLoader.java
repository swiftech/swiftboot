package org.swiftboot.sheet.meta;

import org.swiftboot.util.IoUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Simple picture loader which read from a local file.
 *
 * @author swiftech
 */
public class PictureFileLoader implements PictureLoader {

    private final File file;

    private final int fileType;

    /**
     * @param file
     * @param fileType Workbook.PICTURE_TYPE_JPEG or Workbook.PICTURE_TYPE_PNG
     */
    public PictureFileLoader(File file, int fileType) {
        this.file = file;
        this.fileType = fileType;
    }

    @Override
    public Picture get() throws IOException {
        try (FileInputStream fins = new FileInputStream(file)) {
            byte[] bytes = IoUtils.readAllToBytes(fins);
            return new Picture(this.fileType, bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
