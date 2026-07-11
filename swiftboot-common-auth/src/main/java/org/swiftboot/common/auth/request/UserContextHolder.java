package org.swiftboot.common.auth.request;

/**
 * Context for the user.
 *
 * @since 3.2
 */
public class UserContextHolder {

    private static final ThreadLocal<String> clientSourceThreadLocal = new ThreadLocal<>();

    public static void setClientSource(String clientSource) {
        clientSourceThreadLocal.set(clientSource);
    }

    public static String getClientSource() {
        return clientSourceThreadLocal.get();
    }

    public static void removeClientSource() {
        clientSourceThreadLocal.remove();
    }
}
