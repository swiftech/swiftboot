package org.swiftboot.web.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.util.WebUtils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

/**
 * @author swiftech 2019-08-29
 **/
public class HttpServletCookieUtils {

    public static String getCookieValue(HttpServletRequest request, String name) {
        Cookie cookieToken = WebUtils.getCookie(request, name);
        if (cookieToken == null) {
            return null;
        }
        return cookieToken.getValue();
    }

    /**
     * Try to get value from header, if not exists, try to get it from cookie,
     * return null if failed.
     *
     * @param request
     * @param name
     * @return
     */
    public static String getValueFromHeaderOrCookie(HttpServletRequest request, String name) {
        String value = request.getHeader(name);
        if (StringUtils.isBlank(value)) {
            value = getCookieValue(request, name);
            if (StringUtils.isNotBlank(value)) {
                return value;
            }
        }
        else {
            return value;
        }
        return null;
    }


}
