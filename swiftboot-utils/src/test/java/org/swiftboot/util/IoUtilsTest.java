package org.swiftboot.util;

import org.apache.commons.codec.binary.StringUtils;
import org.junit.jupiter.api.Test;

import java.io.InputStream;

/**
 * @author swiftech 2019-03-26
 **/
public class IoUtilsTest {

    @Test
    public void testReadAll() {
        InputStream resourceAsStream =
                this.getClass().getResourceAsStream("/IoUtilsTest.txt");
        String s = IoUtils.readAllToString(resourceAsStream);
        System.out.println("/" + s + "/");
        byte[] bytesUsAscii = StringUtils.getBytesUsAscii(s);
        System.out.println(org.apache.commons.lang3.StringUtils.join(bytesUsAscii, '\\'));
        System.out.println();

    }
}
