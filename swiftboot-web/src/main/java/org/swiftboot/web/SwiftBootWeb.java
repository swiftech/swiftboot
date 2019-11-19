package org.swiftboot.web;

import org.swiftboot.util.Info;

import java.util.Locale;

/**
 * @deprecated to SpringBoot's facilities
 *
 */
public class SwiftBootWeb {

    static {
        Info.register("swiftboot-web", "/swiftboot-web");
    }

    public static void main(String[] args) {
        Info.validateProperties(Locale.ENGLISH);
        Info.validateProperties(Locale.CHINESE);
    }
}
