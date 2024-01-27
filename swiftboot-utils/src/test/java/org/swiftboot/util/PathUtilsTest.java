package org.swiftboot.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author allen
 */
public class PathUtilsTest {

    @Test
    public void isAbsolutePath() {
        Assertions.assertTrue(PathUtils.isAbsolutePath("C:\\windows\\"));
        Assertions.assertTrue(PathUtils.isAbsolutePath("/usr/var"));
        Assertions.assertFalse(PathUtils.isAbsolutePath("usr/var"));
        Assertions.assertFalse(PathUtils.isAbsolutePath("./usr/var"));
    }

    @Test
    public void isWindowsPath() {
        Assertions.assertTrue(PathUtils.isWindowsPath("A:\\windows\\"));
        Assertions.assertTrue(PathUtils.isWindowsPath("Z:\\windows\\"));
        Assertions.assertTrue(PathUtils.isWindowsPath("a:\\windows\\"));
        Assertions.assertTrue(PathUtils.isWindowsPath("Z:\\windows\\"));
        Assertions.assertFalse(PathUtils.isWindowsPath("/usr/var"));
    }
}
