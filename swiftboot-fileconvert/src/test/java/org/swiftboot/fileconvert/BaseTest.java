package org.swiftboot.fileconvert;

import org.apache.commons.lang3.SystemUtils;

import java.io.File;

/**
 * @author swiftech
 **/
public class BaseTest {

    static final File targetDir;

    static FileConverter fileConverter;

    static {
        File javaIoTmpDir = SystemUtils.getJavaIoTmpDir();
        targetDir = new File(javaIoTmpDir, "swiftboot-fileconvert");
        if (!targetDir.exists()) {
            targetDir.mkdirs();
        }

        fileConverter = new FileConverter();
    }
}
