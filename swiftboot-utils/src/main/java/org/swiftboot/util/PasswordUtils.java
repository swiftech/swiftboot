package org.swiftboot.util;

import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * @author Allen 2019-07-30
 **/
public class PasswordUtils {

    /**
     * 创建密码
     * @param original
     * @return
     */
    public String createPassword(String original) {
        return CryptoUtils.md5(original);
    }

    /**
     * 验证密码
     * @param toBeVerified
     * @param encrypted
     * @return
     */
    public boolean verifyPassword(String toBeVerified, String encrypted) {
        if (StringUtils.isBlank(toBeVerified)) {
            return false;
        }
        return Objects.equals(CryptoUtils.md5(toBeVerified), encrypted);
    }
}
