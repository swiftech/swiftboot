package org.swiftboot.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;

/**
 * @author swiftech
 */
public class ClasspathResourceUtilsTest {


    @Test
    public void testGetAllResourceFiles() {
        File[] files = ClasspathResourceUtils.getResourceFiles("init");
        for (File file : files) {
            Assertions.assertTrue(file.getName().startsWith("resource_file") && file.getName().endsWith("txt"));
        }
    }
}
