package org.swiftboot.util;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * @author swiftech
 **/
public class CryptoUtils {

    /**
     * 加盐 MD5(16进制表示）
     *
     * @param src
     * @param salt
     * @return
     */
    public static String md5salt(String src, String salt) {
        if (StringUtils.isBlank(salt)) {
            return md5(src);
        }
        return md5(src + salt);
    }

    /**
     * 16进制表示的 MD5
     *
     * @param src
     * @return
     */
    public static String md5(String src) {
        if (StringUtils.isBlank(src)) {
            throw new IllegalArgumentException(src);
        }
        byte[] md5 = DigestUtils.md5((src).getBytes());
        if (md5 == null) {
            return null;
        }
        return new String(Hex.encodeHex(md5));
    }

    public static String md5(byte[] src) {
        if (ArrayUtils.isEmpty(src)) {
            throw new IllegalArgumentException();
        }
        byte[] md5 = DigestUtils.md5(src);
        if (md5 == null) {
            return null;
        }
        return new String(Hex.encodeHex(md5));
    }
}
