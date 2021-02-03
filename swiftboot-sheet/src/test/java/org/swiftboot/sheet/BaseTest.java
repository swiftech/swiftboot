package org.swiftboot.sheet;

import org.apache.commons.lang3.SystemUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.swiftboot.sheet.meta.PictureLoader;
import org.swiftboot.sheet.meta.Picture;
import org.swiftboot.util.IoUtils;

import java.io.*;
import java.net.URL;

/**
 * @author allen
 */
public class BaseTest {
    /**
     * Load template file from "/exp"
     *
     * @param fileType
     * @return
     * @throws IOException
     */
    protected InputStream loadTemplate(String fileType) throws IOException {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        String fileUri = "exp/template." + fileType;
        URL url = loader.getResource(fileUri);
        if (url == null) {
            throw new RuntimeException("Template file not found: " + fileUri);
        }
        System.out.println("Load template from: " + url.getPath());
        return url.openStream();
    }

    /**
     * Create a file stream by file type for testing output sheet.
     *
     * @param fromTemplate
     * @param fileType
     * @return
     * @throws FileNotFoundException
     */
    protected OutputStream createOutputStream(boolean fromTemplate, String fileType) throws FileNotFoundException {
        String fileName = String.format("exported%s.%s", fromTemplate ? "_template" : "", fileType);

        File dir = new File(SystemUtils.getUserHome(), "Temp/swiftboot/swiftboot-sheet/");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File f = new File(dir, fileName);
        System.out.println("Prepared to export data to file: " + f.toString());
        return new FileOutputStream(f.toString());
    }

    /**
     * Load the picture file for export testing.
     */
    protected PictureLoader pictureLoader = () -> {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        URL url = loader.getResource("exp/exp.jpg");
        if (url == null) {
            throw new RuntimeException("Picture file not found to export");
        }
        byte[] bytes = IoUtils.readAllToBytes(url.openStream());
        return new Picture(Workbook.PICTURE_TYPE_JPEG, bytes);
    };
}
