package org.swiftboot.shiro.session;

import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;

/**
 * @author swiftech
 * @since 1.2
 **/
public class ShiroSessionUtils {

//    static Logger log = LoggerFactory.getLogger(ShiroSessionUtils.class);

    public static void printSession(Session session) {
        System.out.printf("  Session: %s(%s)%n", session.getId(), session.getHost());
//        System.out.println("  Session authorized: " + SecurityUtils.getSubject().isAuthenticated());
        System.out.println(String.format("  Session expires in %d(%s) minutes", expiresInMinutes(session), expiresAt(session)));
        for (Object attributeKey : session.getAttributeKeys()) {
            System.out.println(String.format("  Session attr: %s - %s", attributeKey, session.getAttribute(attributeKey)));
        }
    }

    /**
     * 计算会话在多少分钟后超时
     *
     * @param session
     * @return
     */
    public static long expiresInMinutes(Session session) {
        return session.getTimeout() / (60 * 1000);
    }

    /**
     * 计算会话在什么时间点超时，用 'yyyy-MM-dd HH:mm:ss' 格式表示
     *
     * @param session
     * @return
     */
    public static String expiresAt(Session session) {
        return FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss")
                .format(System.currentTimeMillis() + session.getTimeout());
    }
}
