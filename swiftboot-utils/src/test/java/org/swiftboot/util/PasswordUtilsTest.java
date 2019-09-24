package org.swiftboot.util;

import org.junit.jupiter.api.Test;

public class PasswordUtilsTest {

    @Test
    public void test() {
        System.out.println(PasswordUtils.createPassword("123456"));
    }
}
