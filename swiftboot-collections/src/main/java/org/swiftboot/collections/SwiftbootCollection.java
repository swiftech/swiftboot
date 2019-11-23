package org.swiftboot.collections;

import org.swiftboot.util.Info;

import java.util.Locale;

public class SwiftbootCollection {
    static {
        Info.register("/swiftboot-collections", R.class);
    }

    public static void main(String[] args) {
        Info.validateProperties(Locale.ENGLISH);
        Info.validateProperties(Locale.SIMPLIFIED_CHINESE);
    }
}
