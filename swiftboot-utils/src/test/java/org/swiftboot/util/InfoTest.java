package org.swiftboot.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class InfoTest {

    @Test
    public void test() {
        Assertions.assertEquals("SwiftBoot-utils", Info.get());
    }
}
