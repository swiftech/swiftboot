package org.swiftboot.collections;

import org.swiftboot.util.Info;

public class SwiftbootCollection {
    static {
        Info.register("swiftboot-collection", "/swiftboot-collections");
    }

    public static void main(String[] args) {
        Info.validateProperties();
    }
}
