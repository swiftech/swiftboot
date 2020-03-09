package org.swiftboot.demo.init;

/**
 * @author swiftech
 */
public class PermissionCodeUtils {

    public static String standardPermCode(String permCode) {
        if ("*".equals(permCode)) {
            return permCode; // root
        }
        if (!permCode.endsWith(":*")) {
            return permCode + ":*";
        }
        return permCode;
    }
}
