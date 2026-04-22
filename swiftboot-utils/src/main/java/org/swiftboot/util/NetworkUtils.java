package org.swiftboot.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @since 3.1.2
 */
public class NetworkUtils {

    /**
     *
     * @param ip
     * @return
     */
    public static boolean isLocalOrPrivateIp(String ip) {
        try {
            InetAddress addr = InetAddress.getByName(ip);
            return addr.isLoopbackAddress()
                    || addr.isLinkLocalAddress()
                    || addr.isSiteLocalAddress();
        } catch (UnknownHostException e) {
            return false;
        }
    }
}
