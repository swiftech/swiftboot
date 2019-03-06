package org.swiftboot.util;

import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author switech
 **/
public class SysUtils {

    /**
     * 从系统环境变量或者虚拟机环境中获得参数值
     * @param key
     * @return
     */
    public static String getSysParam(String key) {
        String ret = System.getenv(key);
        if (StringUtils.isBlank(ret)) {
            ret = System.getProperty(key);
        }
        return ret;
    }
}
