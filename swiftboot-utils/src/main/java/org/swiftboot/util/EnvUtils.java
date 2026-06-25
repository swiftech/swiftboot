package org.swiftboot.util;

public class EnvUtils {

    /**
     *
     * @param evnName
     * @return
     * @since 3.1.5
     */
    public static boolean isEnvValTrue(String evnName) {
        String v = System.getenv(evnName);
        if (v == null) {
            return false;
        }
        return Boolean.parseBoolean(v);
    }

}
