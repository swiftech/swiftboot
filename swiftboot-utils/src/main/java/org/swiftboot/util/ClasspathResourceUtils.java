package org.swiftboot.util;

import java.io.File;
import java.net.URL;

/**
 * @author swiftech
 * @since 1.1
 */
public class ClasspathResourceUtils {

    /**
     * 从资源路径表示的 CLASSPATH 目录中检索并返回用关键字过滤后缀的所有文件
     *
     * @param resourceUri 资源路径 URI，开头和结尾都都没有 "/"
     * @param fileSuffix  用 suffix 过滤文件后缀
     * @return
     */
    public static File[] getResourceFiles(String resourceUri, String fileSuffix) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        URL url = loader.getResource(resourceUri);
        if (url == null) {
            return null;
        }
        String path = url.getPath();
        return new File(path).listFiles((dir, name) -> name.endsWith(fileSuffix));
    }

    /**
     * 获取指定资源文件夹下的所有文件（不能用于 jar 或者 war 文件）
     *
     * @param resourceUri 资源路径 URI，开头和结尾都都没有 "/"
     * @return
     */
    public static File[] getResourceFiles(String resourceUri) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        URL url = loader.getResource(resourceUri);
        if (url == null) {
            return null;
        }
        String path = url.getPath();
        return new File(path).listFiles();
    }

}
