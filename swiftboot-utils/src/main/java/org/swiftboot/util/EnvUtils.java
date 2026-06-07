package org.swiftboot.util;

import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

public class EnvUtils {


    public static void printSystemProperties() {
        displaySystemPropsInStdout("", new SysPropertyExtractor() {
            @Override
            public boolean matched(Entry propEntry, String str) {
                return propEntry.getKey().toString().contains(str);
            }
        });
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
        for (Entry entry : props.entrySet()) {
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
    @FunctionalInterface
    public interface SysPropertyExtractor {

        /**
         *
         * @param propEntry
         * @param str
         * @return
         */
        boolean matched(Entry propEntry, String str);
    }
}
