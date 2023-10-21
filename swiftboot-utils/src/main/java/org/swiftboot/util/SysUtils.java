package org.swiftboot.util;

import org.apache.commons.lang3.StringUtils;

/**
 * @author swiftech
 **/
public class SysUtils {

    /**
     * 从系统环境变量或者虚拟机环境中获得参数值
     *
     * @param key
     * @return
     * @deprecated use {@link org.apache.commons.lang3.SystemUtils}
     */
    public static String getSysParam(String key) {
        String ret = System.getenv(key);
        if (StringUtils.isBlank(ret)) {
            ret = System.getProperty(key);
        }
        return ret;
    }

    /**
     * Whether a system property's value is true.
     *
     * @param evnName
     * @return
     * @since 2.4.5
     */
    public static boolean isSystemPropValTrue(String evnName) {
        String v = System.getProperty(evnName);
        if (v == null) {
            return false;
        }
        return Boolean.parseBoolean(v);
    }
}
