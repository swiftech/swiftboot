package org.swiftboot.util;

import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

/**
 * @author Allen 2019-02-27
 **/
public class IdUtils {

    /**
     * 生成不带UUID，不包含'-'分隔符
     *
     * @return
     */
    public static String makeUUID() {
        String uuid = UUID.randomUUID().toString();
        return StringUtils.replaceChars(uuid, "-", "");
    }
}