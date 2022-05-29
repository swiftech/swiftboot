package org.swiftboot.web.util;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * @author swiftech
 */
public class HeaderUtils {

    /**
     * Parse the cookie content in Headers and return specific cookie value.
     *
     * @param cookieStrInHeader
     * @param cookieName
     * @return
     */
    public static String parseCookieAndGet(String cookieStrInHeader, String cookieName) {
        String[] all = StringUtils.split(cookieStrInHeader, ';');
        for (String entry : all) {
            String[] kv = StringUtils.split(entry.trim(), '=');
            if (ArrayUtils.isEmpty(kv) || kv.length < 2) {
                continue;
            }
            if (cookieName.equals(kv[0].trim())) {
                return kv[1].trim();
            }
        }
        return null;
    }
}
