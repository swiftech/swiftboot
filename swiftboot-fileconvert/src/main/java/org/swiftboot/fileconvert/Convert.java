package org.swiftboot.fileconvert;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * @author swiftech
 **/
public interface Convert {

    String[] supportedSourceFileType();

    String targetFileType();

    List<OutputStream> convert(InputStream inputStream, ConvertCallback convertCallback) throws ConvertException;
}
