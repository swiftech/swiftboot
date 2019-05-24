package org.swiftboot.fileconvert.impl;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
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
public class Html2PdfConvert implements Convert {

    @Override
    public String[] supportedSourceFileType() {
        return new String[]{"html"};
    }

    @Override
    public String targetFileType() {
        return "pdf";
    }

    @Override
    public List<OutputStream> convert(InputStream inputStream, ConvertCallback convertCallback) throws ConvertException {
        Document document = new Document();
        PdfWriter writer = null;
        OutputStream outputStream;
        try {
            outputStream = convertCallback.onPage(0);
            writer = PdfWriter.getInstance(document, outputStream);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ConvertException();
        }
        document.open();
        try {
            XMLWorkerHelper.getInstance().parseXHtml(writer, document, inputStream);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ConvertException();
        }
        document.close();
        return new ArrayList<OutputStream>() {
            {
                add(outputStream);
            }
        };
    }
}
