package org.swiftboot.fileconvert.impl;

import org.apache.commons.lang3.SystemUtils;
import org.swiftboot.fileconvert.Convert;
import org.swiftboot.fileconvert.ConvertCallback;
import org.swiftboot.fileconvert.ConvertException;

import java.io.*;
import java.util.List;

/**
 * @author swiftech
 **/
public class Word2ImageConvert implements Convert {
    @Override
    public String[] supportedSourceFileType() {
        return new String[0];
    }

    @Override
    public String targetFileType() {
        return null;
    }

    @Override
    public List<OutputStream> convert(InputStream inputStream, ConvertCallback convertCallback) throws ConvertException {
        // 先转为PDF
        WordDocx2PdfConvert wordDocx2PdfConvert = new WordDocx2PdfConvert();
        File tempFile = new File(SystemUtils.getJavaIoTmpDir(), "target.pdf");
        wordDocx2PdfConvert.convert(inputStream, new ConvertCallback() {
            @Override
            public OutputStream onPage(int page) throws Exception {
                return new FileOutputStream(tempFile);
            }
        });
        // 再转为图片
        Pdf2ImageConvert pdf2ImageConvert = new Pdf2ImageConvert();
        List<OutputStream> converted = null;
        try {
            converted = pdf2ImageConvert.convert(new FileInputStream(tempFile), convertCallback);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new ConvertException();
        }
        return converted;
    }
}
