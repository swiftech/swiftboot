package org.swiftboot.util;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;

/**
 * @author swiftech
 */
public class ClasspathResourceUtilsTest {

    @Test
    public void testxx1() {
        File[] resourceFolderFiles =
                ClasspathResourceUtils.getResourceFilesForce(StringUtils.class, "");
        for (File file : resourceFolderFiles) {
            System.out.println(file);
        }
    }

    @Test
    public void testxx2() {

        File[] resourceFolderFiles = ClasspathResourceUtils.getResourceFilesForce(ClasspathResourceUtils.class, "zh_CN.properties");
        for (File file : resourceFolderFiles) {
            System.out.println(file);
        }
    }


    @Test
    public void testGetAllResourceFiles() {
        File[] files = ClasspathResourceUtils.getResourceFiles("init");
        for (File file : files) {
            Assertions.assertTrue(file.getName().startsWith("resource_file") && file.getName().endsWith("txt"));
        }
    }
}
