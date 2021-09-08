package org.swiftboot.web.util;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;

import java.util.List;
import java.util.Optional;

/**
 * @author swiftech
 * @since 2.1
 */
public class SpringWebUtils {

    /**
     * Get specific cookie value from http header for Spring Web application.
     *
     * @param cookieName
     * @param httpInputMessage
     * @return
     */
    public static String getCookieFromHeader(String cookieName, HttpInputMessage httpInputMessage) {
        List<String> cookies = httpInputMessage.getHeaders().get("cookie");
        if (cookies == null || cookies.isEmpty()) {
            return null;
        }
        String cookieStr = cookies.stream().findFirst().get();
        return HeaderUtils.parseCookieAndGet(cookieStr, cookieName);
    }


    public static String getHeader(String headerName, HttpInputMessage httpInputMessage) {
        HttpHeaders headers = httpInputMessage.getHeaders();
        List<String> headValues = headers.get(headerName);
        if (!CollectionUtils.isEmpty(headValues)) {
            Optional<String> value = headValues.stream().findFirst();
            if (value.isPresent()) {
                return value.get();
            }
        }
        return null;
    }
}
