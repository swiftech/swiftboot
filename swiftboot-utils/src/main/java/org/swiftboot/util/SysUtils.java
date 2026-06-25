package org.swiftboot.util;

import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.TreeMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author swiftech
 **/
public class SysUtils {

    /**
     * 从系统环境变量或者虚拟机环境中获得参数值
     *
     * @param key
     * @return
     * @deprecated use {@link org.apache.commons.lang3.SystemUtils}
     */
    public static String getSysParam(String key) {
        String ret = System.getenv(key);
        if (StringUtils.isBlank(ret)) {
            ret = System.getProperty(key);
        }
        return ret;
    }

    /**
     * Whether a system property's value is true.
     *
     * @param evnName
     * @return
     * @since 2.4.5
     */
    public static boolean isSystemPropValTrue(String evnName) {
        String v = System.getProperty(evnName);
        if (v == null) {
            return false;
        }
        return Boolean.parseBoolean(v);
    }

    /**
     * Get all properties from JVM.
     *
     * @return
     * @since 3.1.5
     */
    public static Map<String, Object> getSysProps() {
        Properties props = System.getProperties();
        return props.entrySet().stream().collect(Collectors.toMap(e -> e.getKey().toString(), Entry::getValue));
    }

    /**
     * Get properties filtered by key prefix from JVM.
     *
     * @param prefix
     * @return
     * @since 3.1.5
     */
    public static Map<String, Object> getSysProps(String prefix) {
        Properties props = System.getProperties();
        return props.entrySet().stream().filter(e -> e.getKey().toString().startsWith(prefix))
                .collect(Collectors.toMap(e -> e.getKey().toString(), Entry::getValue));
    }

    /**
     * Get properties filtered by predictor from JVM.
     *
     * @param predictor
     * @return
     * @since 3.1.5
     */
    public static Map<String, Object> getSysProps(Predicate<Entry<Object, Object>> predictor) {
        Properties props = System.getProperties();
        Map<String, Object> ret = new TreeMap<>();
        for (Entry<Object, Object> entry : props.entrySet()) {
            if (predictor == null || predictor.test(entry)) {
                ret.put(entry.getKey().toString(), entry.getValue());
            }
        }
        return ret;
    }


    /**
     * Print the JVM properties for debugging.
     *
     * @param props
     * @since 3.1.5
     */
    public static void printSysProps(Map<String, Object> props) {
        System.out.println(" ==================================================");
        if (!props.isEmpty()) {
            props.forEach((k, v) -> System.out.printf("sys prop: %s = %s%n", k, v));
        }
        else {
            System.out.println("No system properties found to display");
        }
        System.out.println(" ==================================================");
    }
}
