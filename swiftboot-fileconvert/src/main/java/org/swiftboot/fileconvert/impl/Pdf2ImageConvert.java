package org.swiftboot.fileconvert.impl;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;
import org.swiftboot.fileconvert.Convert;
import org.swiftboot.fileconvert.ConvertCallback;
import org.swiftboot.fileconvert.ConvertException;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author swiftech
 **/
public class Pdf2ImageConvert implements Convert {

    @Override
    public String[] supportedSourceFileType() {
        return new String[]{"pdf"};
    }

    @Override
    public String targetFileType() {
        return "jpg";
    }

    @Override
    public List<OutputStream> convert(InputStream inputStream, ConvertCallback convertCallback) throws ConvertException {
        PDDocument document = null;
        try {
            document = PDDocument.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ConvertException();
        }
        PDFRenderer pdfRenderer = new PDFRenderer(document);
        List<OutputStream> ret = new ArrayList<>();
        try {
            for (int page = 0; page < document.getNumberOfPages(); ++page) {
                BufferedImage bim = pdfRenderer.renderImageWithDPI(page, 300, ImageType.RGB);
                OutputStream outputStream = null;
                try {
                    outputStream = convertCallback.onPage(page);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new ConvertException();
                }
                ImageIOUtil.writeImage(bim, "jpg", outputStream, 300);
                ret.add(outputStream);
            }
            document.close();
            return ret;
        } catch (IOException e) {
            e.printStackTrace();
            return ret;
        }
    }
}
