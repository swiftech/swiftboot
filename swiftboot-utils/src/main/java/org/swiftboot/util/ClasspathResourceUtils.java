package org.swiftboot.util;

import java.io.File;
import java.net.URL;

/**
 * @author swiftech
 */
public class ClasspathResourceUtils {

    /**
     *
     * @param resourceUri 资源路径 URI，开头和结尾都都没有 "/"
     * @param fileSuffix 用 suffix 过滤文件后缀
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

    /**
     * TODO move to test
     *
     * @param args
     */
    public static void main(String[] args) {
        File[] resourceFolderFiles = getResourceFiles("init");
        for (File resourceFolderFile : resourceFolderFiles) {
            System.out.println(resourceFolderFile);
        }


    }
}
