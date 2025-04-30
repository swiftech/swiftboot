package org.swiftboot.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;

/**
 * @since 3.0
 */
public class ImageUtils {

    /**
     *
     * @param srcFile
     * @param scale
     * @param targetFile
     * @return
     * @throws IOException
     */
    public static boolean scaleImage(File srcFile, double scale, File targetFile) throws IOException {
        if (!srcFile.exists()) {
            throw new FileNotFoundException("文件不存在");
        }
        try {
            BufferedImage inputImage = ImageIO.read(srcFile);
            int width = 0;
            int height = 0;
            if (scale > 0) {
                width = inputImage.getWidth(null);
                height = inputImage.getHeight(null);
            }

            int newWidth = new BigDecimal(scale).multiply(new BigDecimal(width)).intValue();
            int newHeight = new BigDecimal(scale).multiply(new BigDecimal(height)).intValue();

            BufferedImage bufferedImage = new BufferedImage(newWidth, newHeight, inputImage.getType());

            Graphics2D graphics = bufferedImage.createGraphics();
            graphics.drawImage(inputImage, 0, 0, newWidth, newHeight, null);
            graphics.dispose();
            ImageIO.write(bufferedImage, "jpg", targetFile);
            return true;
        } catch (Exception ef) {
            ef.printStackTrace();
            return false;
        }
    }
}
