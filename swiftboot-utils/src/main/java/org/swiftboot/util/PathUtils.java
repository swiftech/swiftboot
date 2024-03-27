package org.swiftboot.util;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.regex.Pattern;

/**
 * @since 2.4.5
 */
public class PathUtils {
    private static final String PATH_BASE_WIN = "[a-zA-Z]:\\\\";
    private static final Pattern winPathPattern = Pattern.compile(PATH_BASE_WIN);

    public static boolean isWindowsPath(String path) {
        return winPathPattern.matcher(path).find();
    }

    public static boolean isAbsolutePath(String path) {
        return StringUtils.startsWith(path, "/") || isWindowsPath(path);
    }

    /**
     * @param folder
     * @param file
     * @return
     */
    public static boolean isParentFolder(File folder, File file) {
        String parentFolderPath = StringUtils.endsWith(folder.getPath(), File.separator)
                ? folder.getPath()
                : folder.getPath() + File.separator;
        return StringUtils.startsWith(file.getPath(), parentFolderPath);
    }

    /**
     * Get real relative path even not in the save directory.
     *
     * @param file
     * @param ancestorDir
     * @return like "a/b/c" or "../a/b/c"
     */
    public static String getRelativePath(File file, File ancestorDir) {
        return ancestorDir.toPath().relativize(file.toPath()).toString();
    }

    /**
     * Get real relative path even not in the save directory.
     *
     * @param path
     * @param ancestorPath
     * @return like "a/b/c" or "../a/b/c"
     */
    public static String getRelativePath(String path, String ancestorPath) {
        return new File(ancestorPath).toPath().relativize(new File(path).toPath()).toString();
//        return Path.of(ancestorPath).relativize(Path.of(path)).toString();  // java 11+
    }
}
