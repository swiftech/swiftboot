package org.swiftboot.util;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * 
 * @author swiftech
 **/
public class CryptoUtils {

    /**
     * 加盐 MD5
     *
     * @param src
     * @param salt
     * @return
     */
    public static String md5salt(String src, String salt) {
        return md5(src + salt);
    }

    /**
     * MD5
     *
     * @param src
     * @return
     */
    public static String md5(String src) {
        byte[] md5 = DigestUtils.md5((src).getBytes());
        if (md5 == null) {
            return null;
        }
        return new String(Hex.encodeHex(md5));
    }
}
