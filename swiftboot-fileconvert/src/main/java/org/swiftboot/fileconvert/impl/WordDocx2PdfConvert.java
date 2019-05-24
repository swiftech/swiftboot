package org.swiftboot.fileconvert.impl;

import fr.opensagres.poi.xwpf.converter.pdf.PdfConverter;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.swiftboot.fileconvert.Convert;
import org.swiftboot.fileconvert.ConvertCallback;
import org.swiftboot.fileconvert.ConvertException;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author swiftech
 **/
public class WordDocx2PdfConvert implements Convert {
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
        try {
            XWPFDocument document = new XWPFDocument(inputStream);
            OutputStream outputStream = convertCallback.onPage(0);
            PdfConverter.getInstance().convert(document, outputStream, null);
            return new ArrayList<OutputStream>() {
                {
                    add(outputStream);
                }
            };
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
