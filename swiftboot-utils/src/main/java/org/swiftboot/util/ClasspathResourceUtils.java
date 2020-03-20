package org.swiftboot.util;

import org.apache.commons.lang3.NotImplementedException;

import java.io.File;
import java.net.URL;
import java.security.CodeSource;

/**
 *
 * @author swiftech
 * @since 1.1
 */
public class ClasspathResourceUtils {

    /**
     * 从 CLASSPATH 根目录检索并返回用关键字过滤后缀的所有文件，无论是 classes 目录还是 jar 文件
     * TODO 思考是否需要，并修改为支持路径递归的
     * @param sourceClass 来源类，必须和资源文件在一个 jar 文件中
     * @param fileSuffix
     * @return
     */
    public static File[] getResourceFilesForce(Class<?> sourceClass, String fileSuffix) {
        CodeSource codeSource = sourceClass.getProtectionDomain().getCodeSource();
        if (codeSource != null) {
            URL location = codeSource.getLocation();
            return new File(location.getPath()).listFiles((dir, name) -> name.endsWith(fileSuffix));
        }
        return null;
    }

    public static File[] getResourceFilesForce(String resourceUri, String fileSuffix) {
        throw new NotImplementedException("Need more effect");
    }

    /**
     * 从资源路径表示的 CLASSPATH 目录中检索并返回用关键字过滤后缀的所有文件
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
