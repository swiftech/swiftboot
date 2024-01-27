package org.swiftboot.util;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * @since 2.4.5
 */
public class UrlUtils {

    /**
     * Check whether the URL is valid.
     *
     * @param url
     * @return
     */
    public static boolean isValid(String url) {
        try {
            new URL(url).toURI();
            return true;
        } catch (URISyntaxException | MalformedURLException e) {
            return false;
        }
    }
}
