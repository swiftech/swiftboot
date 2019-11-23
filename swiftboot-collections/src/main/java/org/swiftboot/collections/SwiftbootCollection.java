package org.swiftboot.collections;

import org.swiftboot.util.Info;

import java.util.Locale;

public class SwiftbootCollection {
    static {
        Info.register("swiftboot-collection", "/swiftboot-collections");
    }

    public static void main(String[] args) {
        Info.validateProperties(Locale.ENGLISH);
        Info.validateProperties(Locale.SIMPLIFIED_CHINESE);
    }
}
