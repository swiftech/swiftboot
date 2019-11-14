package org.swiftboot.util;

import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

public class EnvUtils {


    public static void main(String[] args) {
//        printSystemProperties();

        displaySystemPropsInStdout("", new SysPropertyExtractor() {
            @Override
            public boolean matched(Map.Entry propEntry, String str) {
                return propEntry.getKey().toString().contains(str);
            }
        });
    }

    public static void printSystemProperties() {
        displaySystemPropsInStdout("", new SysPropertyExtractor() {
            @Override
            public boolean matched(Map.Entry propEntry, String str) {
                return propEntry.getKey().toString().contains(str);
            }
        });
//        Properties properties = System.getProperties();
//        Enumeration<Object> keys = properties.keys();
//        while (keys.hasMoreElements()) {
//            Object key = keys.nextElement();
//            System.out.println("  " + key + "=" + properties.getProperty(String.valueOf(key)));
//        }
    }

    /**
     * Display system props in stdout which starting with specified keyword. NULL or empty prefix means all.
     *
     * @param keyword
     * @param extractor extractor closure, accept any if null.
     */
    public static int displaySystemPropsInStdout(String keyword, SysPropertyExtractor extractor) {
        Properties props = System.getProperties();
        // TODO to sort properties if required.
        Set<String> propsList = new TreeSet<>();
        for (Map.Entry entry : props.entrySet()) {
            if (extractor == null || extractor.matched(entry, keyword)) {
                propsList.add(" sys prop: " + entry.getKey() + " = " + entry.getValue());
            }
        }
        System.out.println(" ==================================================");
        if (!propsList.isEmpty()) {
            for (String prop : propsList) {
                System.out.println(prop);
            }
        }
        else {
            System.out.println("No system properties found to display");
        }
        System.out.println(" ==================================================");
        return propsList.size();
    }

    /**
     *
     */
    public interface SysPropertyExtractor {

        /**
         *
         * @param propEntry
         * @param str
         * @return
         */
        boolean matched(Map.Entry propEntry, String str);
    }
}
