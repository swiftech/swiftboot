package org.swiftboot.util;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 *
 */
public class Info {

    static {
        register("swiftboot-utils", "/swiftboot-utils");
    }

    private static Map<String, String> map;

    private static List<Properties> propertiesList;

    public static void register(String key, String propertyFilePath) {
        if (map == null) {
            map = new HashMap<>();
        }
        if (map.containsValue(propertyFilePath)) {
            throw new RuntimeException(String.format("Failed to register %s, because it is already exists", propertyFilePath));
        }
        map.put(key, propertyFilePath);
    }

    public static String get() {
        return get(Info.class, "");
    }

    public static String get(Class clazz, String tag, Object... params) {
        String format = get(clazz, tag);
        if (StringUtils.isNotBlank(format)) {
            return String.format(format, params);
        }
        else {
            return format;
        }
    }

    public static String get(Class clazz, String tag) {
        if (propertiesList == null) {
            propertiesList = new LinkedList<>();
            Locale curLocale = Locale.getDefault();
            System.out.println(curLocale);
            // Load all registered resources
            int total = 0;
            for (String key : map.keySet()) {
                String filePath = map.get(key);
                String resName = String.format("%s_%s.properties", filePath, curLocale.toString());
                System.out.println("from " + resName);
                try {
                    InputStream ins = Info.class.getResourceAsStream(resName);
                    if (ins != null) {
                        Properties p = new Properties();
                        p.load(ins);
                        ins.close();
                        total += p.size();
                        propertiesList.add(p);
                    }
                    else {
                        System.out.println("Failed to load from: " + resName);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            System.out.printf("%d items for %d registered information resources%n", total, map.size());
        }

        String key = clazz.getName();
        if (StringUtils.isNotBlank(tag)) {
            key = key + "." + tag;
        }
        System.out.println("Retrieve " + key);
        for (Properties properties : propertiesList) {
            String value = properties.getProperty(key);
            if (!StringUtils.isBlank(value)) {
                return value;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println("# " + Info.get(IdUtils.class, "id_failed"));
    }
}
