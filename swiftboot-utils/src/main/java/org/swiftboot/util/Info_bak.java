package org.swiftboot.util;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.CodeSource;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Use information resources, each resource is loaded to memory when accessing it.
 */
public class Info_bak {

    /**
     * Initiated and new info added only when needed.
     */
    private static Map<String, String> data;

    private static Properties properties;

    public static String get(Class clazz, String tag) {
        if (properties == null) {
            properties = new Properties();

            try {
                Locale curLocale = Locale.getDefault();
                System.out.println(curLocale);

//                ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
//                Enumeration<URL> resources = classLoader.getResources("org/apache");
//                while (resources.hasMoreElements()) {
//                    URL url = resources.nextElement();
//
//                    InputStream inputStream = url.openStream();
//                    System.out.println(inputStream);
//                    inputStream.read(new byte[1024]);
//                }


                CodeSource codeSource = StringUtils.class.getProtectionDomain().getCodeSource();
                if (codeSource != null) {
                    URL url = codeSource.getLocation();
                    System.out.println(url);
                    InputStream inputStream = url.openStream();
                    System.out.println(inputStream);
                    ZipInputStream zip = new ZipInputStream(inputStream);
                    System.out.println(zip.available());
                    while (true) {
                        ZipEntry nextEntry = zip.getNextEntry();
                        if (nextEntry == null) break;
                        System.out.println(nextEntry.getName());
                    }
                    zip.close();
                }
                else {
                    System.out.println("error");
                }


//
//                String resName = "/swiftboot-utils_" + curLocale.toString() + ".properties";
//                System.out.println("from " + resName);
//                InputStream ins = Info.class.getResourceAsStream(resName);
//                if (ins != null) {
//                    properties.load(ins);
//                    ins.close();
//                    value = properties.getProperty(key);
//                }
//                else {
//                    System.out.println("Failed to load from: " + resName);
//                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String key = clazz.getName() + "." + tag;
        System.out.println("Retrieve " + key);
        String value = properties.getProperty(key);
        if (StringUtils.isBlank(value)) {
            return null;
        }
        else {
            return value;
        }
    }


//
//    public static String get(Class clazz, String tag, String defaultValue) {
//        if (data == null) {
//            data = new HashMap<>();
//        }
//
//        String key = clazz.getName() + "." + tag;
//        System.out.println("Retrieve " + key);
//        String value = data.get(key);
//        if (StringUtils.isBlank(value)) {
//
//            Properties properties = new Properties();
//            try {
//                Locale curLocale = Locale.getDefault();
//
//                String resName = "/swiftboot-utils_" + curLocale.toString() + ".properties";
//                System.out.println("from " + resName);
//                InputStream ins = Info.class.getResourceAsStream(resName);
//                if (ins != null) {
//                    properties.load(ins);
//                    value = properties.getProperty(key);
//                }
//                else {
//                    System.out.println("Failed to load from: " + resName);
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            if (StringUtils.isBlank(value)) {
//                return defaultValue;
//            }
//            else {
//                return value;
//            }
//        }
//        else {
//            return value;
//        }
//    }

    public interface InfoMeta {
        String resourceFileName();
    }

    public static void main(String[] args) {
        System.out.println("# " + Info_bak.get(IdUtils.class, "id_failed"));
    }
}
