package org.swiftboot.sheet.meta;

import java.io.IOException;

/**
 * Picture loader for client to input picture data.
 *
 * @author swiftech
 */
public interface PictureLoader {

    /**
     * Create {@code Picture} with picture type and data in binary.
     *
     * @return
     * @throws IOException
     */
    Picture get() throws IOException;
}
