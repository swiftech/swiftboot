package org.swiftboot.data;

/**
 * @author swiftech
 * @since 1.3
 */
public final class Info extends org.swiftboot.util.Info {

    static {
        org.swiftboot.util.Info.sources = R.getResourceClasses();
    }

    public static String get() {
        return org.swiftboot.util.Info.get();
    }

    public static String get(Class<?> clazz, String tag, Object... params) {
        return org.swiftboot.util.Info.get(clazz, tag, params);
    }

    public static String get(Class<?> clazz, String tag) {
        return org.swiftboot.util.Info.get(clazz, tag);
    }

    public static String get(String tag, Object... params) {
        return org.swiftboot.util.Info.get(tag, params);
    }

    public static String get(String tag) {
        return org.swiftboot.util.Info.get(tag);
    }

}
