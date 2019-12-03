package org.swiftboot.web.util;

import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * @author swiftech 2019-08-29
 *
 **/
public class HttpServletCookieUtils {

    public static String getCookieValue(HttpServletRequest request, String name) {
        Cookie cookieToken = WebUtils.getCookie(request, name);
        if (cookieToken == null) {
            return null;
        }
        return cookieToken.getValue();
    }
}
