package org.swiftboot.sheet.meta;

import java.io.IOException;

/**
 * PictureLoader?
 *
 * @author allen
 */
public interface PictureLoader {

    Picture get() throws IOException;
}
