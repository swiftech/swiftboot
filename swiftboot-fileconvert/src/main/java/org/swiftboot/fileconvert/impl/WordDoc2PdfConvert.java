package org.swiftboot.fileconvert.impl;

import org.swiftboot.fileconvert.Convert;
import org.swiftboot.fileconvert.ConvertCallback;
import org.swiftboot.fileconvert.ConvertException;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * @author swiftech
 **/
public class WordDoc2PdfConvert implements Convert {

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
        throw new NotImplementedException();
    }
}
