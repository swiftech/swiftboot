package org.swiftboot.fileconvert.impl;

import org.apache.commons.imaging.*;
import org.apache.commons.imaging.common.BufferedImageFactory;
import org.swiftboot.fileconvert.Convert;
import org.swiftboot.fileconvert.ConvertCallback;
import org.swiftboot.fileconvert.ConvertException;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author swiftech
 */
public class Image2ImageConvert implements Convert {
    @Override
    public String[] supportedSourceFileType() {
        return new String[]{"jpg"};
    }

    @Override
    public String targetFileType() {
        return "jpg";
    }

    @Override
    public List<OutputStream> convert(InputStream inputStream, ConvertCallback convertCallback) throws ConvertException {
        final Map<String, Object> readParams = new HashMap<>();

        // set optional parameters if you like
        readParams.put(ImagingConstants.BUFFERED_IMAGE_FACTORY,
                new ScaledBufferedImageFactory(0.5));

        final BufferedImage image;
        try {
            image = Imaging.getBufferedImage(inputStream, readParams);
        } catch (ImageReadException | IOException e) {
            e.printStackTrace();
            throw new ConvertException("Read image failed");
        }

        final ImageFormat format = ImageFormats.JPEG;
        final Map<String, Object> writeParams = new HashMap<>();
        try {
            final byte[] bytes = Imaging.writeImageToBytes(image, format, writeParams);
            ByteArrayOutputStream outs = new ByteArrayOutputStream(bytes.length);
            outs.write(bytes);
            return new LinkedList<OutputStream>() {
                {
                    add(outs);
                }
            };
        } catch (ImageWriteException | IOException e) {
            e.printStackTrace();
            throw new ConvertException("Write image file failed");
        }
    }

    public static class ScaledBufferedImageFactory implements BufferedImageFactory {

        private double scale = 1.0f;

        public ScaledBufferedImageFactory(double scale) {
            this.scale = scale;
        }

        @Override
        public BufferedImage getColorBufferedImage(final int width, final int height,
                                                   final boolean hasAlpha) {
            final GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            final GraphicsDevice gd = ge.getDefaultScreenDevice();
            final GraphicsConfiguration gc = gd.getDefaultConfiguration();
            return gc.createCompatibleImage(
                    new BigDecimal(scale).multiply(new BigDecimal(width)).intValue(),
                    new BigDecimal(scale).multiply(new BigDecimal(height)).intValue(),
                    Transparency.TRANSLUCENT);
        }

        @Override
        public BufferedImage getGrayscaleBufferedImage(final int width, final int height,
                                                       final boolean hasAlpha) {
            return getColorBufferedImage(width, height, hasAlpha);
        }
    }
}
