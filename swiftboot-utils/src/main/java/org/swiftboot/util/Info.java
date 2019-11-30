package org.swiftboot.util;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.*;

/**
 * Any module uses this to localization information has to to:
 * 1. put a properties file names as the module name
 * 2. call Info.register() method to register the module with the properties file, file path must starts with "/".
 */
public class Info {

    /**
     * Mapping of resource files and class.
     */
    private static Map<String, Class> map = new HashMap<>();

    private static List<Properties> propertiesList;

    public static void register(String propertyFilePath, Class usingTags) {
        if (map.containsKey(propertyFilePath)) {
            System.out.println(String.format("%s is already registered", propertyFilePath));
            return;
//            throw new RuntimeException(String.format("Failed to register %s, because it is already exists", propertyFilePath));
        }
        map.put(propertyFilePath, usingTags);
    }

    public static String get(Object... params) {
        String format = get(Info.class, "");
        if (StringUtils.isNotBlank(format)) {
            return String.format(format, params);
        }
        else {
            return format;
        }
    }

    public static String get() {
        return get(Info.class, "");
    }

    public static String get(String tag, Object... params) {
        String format = get(tag);
        if (StringUtils.isNotBlank(format)) {
            return String.format(format, params);
        }
        else {
            return format;
        }
    }

    public static String get(String tag) {
        if (propertiesList == null) {
            loadAllFromClassResources();
        }
        return lookup(tag);
    }

    private static String lookup(String tag) {
        for (Properties properties : propertiesList) {
            String value = properties.getProperty(tag);
            if (!StringUtils.isBlank(value)) {
                return value;
            }
        }
        return null;
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
            loadAllFromClassResources();
        }

        String key = clazz.getName();
        if (StringUtils.isNotBlank(tag)) {
            key = key + "." + tag;
        }
//        System.out.println("Retrieve " + key);
        return lookup(key);
    }

    private static void loadAllFromClassResources() {
        propertiesList = new LinkedList<>();
        Locale curLocale = Locale.getDefault();
        // Load all registered resources
        int total = 0;
        if (map.isEmpty()) {
            throw new RuntimeException("No any information resource registered");
        }
        System.out.printf("%d registered resource files to load%n", map.size());
        for (String key : map.keySet()) {
            String filePath = key;
            Class usingClass = map.get(key);
            String resName = String.format("%s_%s.properties", filePath, curLocale.toString());
            System.out.println("Load resource from " + resName);
            try {
                InputStream ins = Info.class.getResourceAsStream(resName);
                if (ins == null) {
                    System.out.println("Failed to load from: " + resName);
                    resName = String.format("%s.properties", filePath);
                    System.out.println("Load resource from " + resName);
                    ins = Info.class.getResourceAsStream(resName);
                    if (ins == null) {
                        throw new RuntimeException("No properties file found");
                    }
                }
                Properties p = new Properties();
                p.load(ins);
                ins.close();
                total += p.size();
                propertiesList.add(p);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(String.format("Load resource %s failed", resName), e);
            }
        }
        System.out.printf("%d items for %d registered information resources%n", total, map.size());
    }

    /**
     * Check all properties are related to a exist class, if not, maybe class package or name has been changed, it
     * may not work appropriately. This method is better to call at build-time instead of run-time.
     */
    public static void validateProperties(Locale locale) {
        Locale.setDefault(locale);
        validateProperties();
    }

    public static void validateProperties() {
        loadAllFromClassResources();
        Map<String, Object> definedTags = new HashMap<>();
        Map<String, Object> usingTags;
        try {
            usingTags = usingTags();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        for (Properties properties : propertiesList) {
            Enumeration<String> enumeration = (Enumeration<String>) properties.propertyNames();
            while (enumeration.hasMoreElements()) {
                String name = enumeration.nextElement();
                String classFullName = StringUtils.substringBeforeLast(name, ".");
                String tagsName = StringUtils.substringAfterLast(name, ".");
                definedTags.put(tagsName, classFullName);
                try {
                    Class.forName(classFullName);
                } catch (Throwable e) {
                    e.printStackTrace();
                    throw new RuntimeException(String.format("One property '[%s]%s' related class is not exist, maybe it's name was changed.", Locale.getDefault(), classFullName));
                }
                if (!usingTags.containsKey(tagsName)) {
                    System.out.printf("WARN: tag '[%s]%s' for is not using, consider remove it%n", Locale.getDefault(), tagsName);
                }
                // check params number
                String description = (String) properties.get(name);
                String num = name.substring(name.length() - 1);
                if (StringUtils.isNumeric(num)) {
                    int placeholderCount = StringUtils.countMatches(description, "%s") + StringUtils.countMatches(description, "%d") + StringUtils.countMatches(description, "%f");
                    if (Integer.parseInt(num) != placeholderCount) {
                        throw new RuntimeException(String.format("Tag name '%s' is not match the placeholder numbers in '[%s]%s'", name, Locale.getDefault(), description));
                    }
                }
            }
        }
        for (String usingTag : usingTags.keySet()) {
            if (!definedTags.containsKey(usingTag)) {
                throw new RuntimeException(String.format("Tag '%s' is using, but not defined in properties for %s", usingTag, Locale.getDefault()));
            }
        }

        System.out.println("All properties are validated");
    }

    /**
     * Extract all using tags from registered Resource class.
     *
     * @return
     * @throws IllegalAccessException
     */
    private static Map<String, Object> usingTags() throws IllegalAccessException {
        Map<String, Object> usingTags = new HashMap<>();
        for (Class clazz : map.values()) {
            List<Field> items = BeanUtils.getStaticFieldsByType(clazz, String.class);
            for (Field item : items) {
                String tag = (String) item.get(null);
                usingTags.put(tag, item);
            }
        }
        return usingTags;
    }

    public static void validateForAllLocale() {
        validateProperties(Locale.ENGLISH);
        validateProperties(Locale.SIMPLIFIED_CHINESE);
    }

}
