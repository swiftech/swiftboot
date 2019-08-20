package org.swiftboot.util;

import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * @author Allen 2019-07-30
 **/
public class PasswordUtils {

    /**
     * 创建密码
     *
     * @param original
     * @return
     */
    public static String createPassword(String original) {
        return CryptoUtils.md5(original);
    }

    /**
     * 加盐 MD5 密码(16进制表示）
     *
     * @param src
     * @param salt
     * @return
     */
    public static String md5salt(String src, String salt) {
        return CryptoUtils.md5salt(src, salt);
    }

    /**
     * 验证密码
     *
     * @param toBeVerified
     * @param encrypted
     * @return
     */
    public static boolean verifyPassword(String toBeVerified, String encrypted) {
        if (StringUtils.isBlank(toBeVerified)) {
            return false;
        }
        return Objects.equals(CryptoUtils.md5(toBeVerified), encrypted);
    }

    /**
     * 验证密码
     *
     * @param toBeVerified
     * @param encrypted
     * @param salt
     * @return
     */
    public static boolean verifyPassword(String toBeVerified, String encrypted, String salt) {
        if (StringUtils.isBlank(toBeVerified)) {
            return false;
        }
        return Objects.equals(CryptoUtils.md5salt(toBeVerified, salt), encrypted);
    }
}
