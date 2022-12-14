package org.swiftboot.util;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.*;

/**
 * Any module uses this to localization information has to do:
 * 1. Create a resource class in module package like com.foo.bar.R which implements Resource interface and add current resource class to the getResourceClasses() return values.
 * 2. Create a properties file names as resource class name like com.foo.bar.R.properties, this file name can be with locale.
 * 3. Create a new Info class extends me, set the {@code Info.sources} field with this resource class:
 *      {@code org.swiftboot.util.Info.sources = R.getResourceClasses();}
 *
 * @author swiftech
 * @since 1.1
 */
public class Info {

    public static Class<?>[] sources = R.getResourceClasses();

    private static List<Properties> propertiesList;

    private static final boolean debug = false;

    private static final Info info = new Info();

    /**
     * TBD
     *
     * @return
     */
    public static String get() {
        return get(Info.class, "");
    }

    /**
     * Ge information by a tag with params for class
     *
     * @param clazz  Class for which you lookup information
     * @param tag    Indicate which information
     * @param params Params in the information
     * @return
     */
    public static String get(Class<?> clazz, String tag, Object... params) {
        String format = get(clazz, tag);
        if (StringUtils.isNotBlank(format)) {
            return String.format(format, params);
        }
        else {
            return format;
        }
    }

    /**
     * Get information by a tag with params for class
     *
     * @param clazz Class for which you lookup information
     * @param tag   Indicate which information
     * @return
     */
    public static String get(Class<?> clazz, String tag) {
//        if (debug) {
//            System.out.println("Information is disabled");
//            return tag;
//        }
        if (propertiesList == null) {
            try {
                info.loadAllResourcesDeclared(sources);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String key = clazz.getName();
        if (StringUtils.isNotBlank(tag)) {
            key = key + "." + tag;
        }
//        System.out.println("Retrieve " + key);
        return info.lookup(key);
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
//        if (disable) {
//            System.out.println("Information is disabled");
//            return tag;
//        }
        if (propertiesList == null) {
            try {
                info.loadAllResourcesDeclared(sources);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return info.lookup(tag);
    }

    private String lookup(String tag) {
        for (Properties properties : propertiesList) {
            String value = properties.getProperty(tag);
            if (!StringUtils.isBlank(value)) {
                return value;
            }
        }
        return null;
    }

    /**
     * Load all resources declared by 'sources' field of this Info class
     *
     * @param sources
     * @throws IOException
     */
    public void loadAllResourcesDeclared(Class<?>[] sources) throws IOException {
        if (sources == null) {
            throw new RuntimeException("No source class defined");
        }
        int entryCount = 0;
        for (Class<?> source : sources) {
            entryCount += loadLocaledResource(source);
        }
        if (debug)
            System.out.printf("%d properties files and %d resource entries loaded.%n", propertiesList.size(), entryCount);
    }

    /**
     * Load resource in root of classpath by locale
     *
     * @param resourceClass
     * @return 加载的 Properties 大小
     * @throws IOException
     */
    public static int loadLocaledResource(Class<?> resourceClass) throws IOException {
        if (debug) System.out.printf("Current locale: %s%n", Locale.getDefault());
        String localeResourceName = String.format("/%s_%s.properties", resourceClass.getName(), Locale.getDefault().toString());
        InputStream ins = resourceClass.getResourceAsStream(localeResourceName);
        if (ins == null) {
            localeResourceName = String.format("/%s.properties", resourceClass.getName());
            ins = resourceClass.getResourceAsStream(localeResourceName);
        }
        if (ins == null) {
            throw new IOException(String.format("Resource file not found: %s", localeResourceName));
        }
        return loadOnePropertiesFile(ins);
    }

    /**
     * 从一个输入流加载 Properties 并添加到集合中
     *
     * @param ins
     * @return 加载的 Properties 大小
     * @throws IOException
     */
    private static int loadOnePropertiesFile(InputStream ins) throws IOException {
        Properties p = new Properties();
        p.load(ins);
        ins.close();
        if (propertiesList == null) {
            propertiesList = new LinkedList<>();
        }
        propertiesList.add(p);
        return p.size();
    }


    public static void validateForAllLocale() {
        try {
            int[] counts = validateProperties(Locale.ENGLISH);
            System.out.printf("%d entries validated for all properties in locale %s, %d of them are using %n", counts[0], Locale.ENGLISH, counts[1]);
            System.out.println("  ---------------  ");
            validateProperties(Locale.SIMPLIFIED_CHINESE);
            System.out.printf("%d entries validated for all properties in locale %s, %d of them are using %n", counts[0], Locale.ENGLISH, counts[1]);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Check all properties are related to a exist class, if not, maybe class package or name has been changed, it
     * may not work appropriately. This method is better to call at build-time instead of run-time.
     *
     * @param locale
     * @return total and using count
     * @throws IOException
     */
    public static int[] validateProperties(Locale locale) throws IOException {
        Locale.setDefault(locale);
        int[] ret = new int[2];
        info.loadAllResourcesDeclared(sources);
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
                ret[0]++;
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
                    System.out.printf("WARN: tag '[%s]%s' is not using, consider remove it%n", Locale.getDefault(), tagsName);
                }
                else {
                    ret[1]++;
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
        return ret;
    }

    /**
     * Extract all using tags from registered Resource class.
     *
     * @return
     * @throws IllegalAccessException
     */
    private static Map<String, Object> usingTags() throws IllegalAccessException {
        Map<String, Object> usingTags = new HashMap<>();
        for (Class<?> clazz : sources) {
            List<Field> items = BeanUtils.getStaticFieldsByType(clazz, String.class);
            for (Field item : items) {
                String tag = (String) item.get(null);
                usingTags.put(tag, item);
            }
        }
        return usingTags;
    }


    /**
     *
     */
    public interface Resource {
    }
}
