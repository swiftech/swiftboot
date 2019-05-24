package org.swiftboot.fileconvert;

import java.io.OutputStream;

/**
 * @author swiftech
 **/
public interface ConvertCallback {

    /**
     * @param page 从0开始的
     */
    OutputStream onPage(int page) throws Exception;
}
