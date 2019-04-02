package org.swiftboot.util;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.UUID;

/**
 * @author swiftech
 **/
public class IdUtils {

    /**
     * 生成不包含'-'分隔符的 UUID，长度固定为32字节
     *
     * @return
     */
    public static String makeUUID() {
        String uuid = UUID.randomUUID().toString();
        return StringUtils.replaceChars(uuid, "-", "");
    }

    /**
     * 以当前时刻生成流水号，长度固定为17字节
     *
     * @return
     */
    public static String makeSn() {
        return DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMddHHmmssSS");
    }

    /**
     * 已当前时刻+三位随机数生成流水号，长度固定为20字节
     *
     * @return
     */
    public static String makeSnRandom() {
        return makeSn() + RandomStringUtils.randomNumeric(3);
    }
}
