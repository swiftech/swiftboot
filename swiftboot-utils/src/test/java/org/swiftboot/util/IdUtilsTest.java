package org.swiftboot.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

/**
 * @author swiftech 2019-04-01
 **/
public class IdUtilsTest {

    @Test
    public void testMakeSn() {
        String s = IdUtils.makeSn();
        System.out.println(s);
        Assertions.assertEquals(17, s.length());
    }

    @Test
    public void testMakeSwiftIdByCode() {
        String s = IdUtils.makeID("teusrore");
        System.out.println(s);
        Assertions.assertEquals(32, s.length());
    }


    @Test
    public void testMakeSwiftIdByCodeAndServerId() {
        String s = IdUtils.makeID("adcdef", "1234");
        System.out.println(s);
        Assertions.assertEquals(32, s.length());

        s = IdUtils.makeID("abcdef", "1234");
        System.out.println(s);
        Assertions.assertEquals(32, s.length());
        s = IdUtils.makeID("abcd", "1234");
        System.out.println(s);
        Assertions.assertEquals(32, s.length());

        s = IdUtils.makeID("abcdef", "12");
        System.out.println(s);
        Assertions.assertEquals(32, s.length());
        s = IdUtils.makeID("abcdef", "123");
        System.out.println(s);
        Assertions.assertEquals(32, s.length());
    }

    @Test
    public void testMakePerformance() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            String s = IdUtils.makeID("abba", "12");
            System.out.println(s);
        }
        long end = System.currentTimeMillis();
        System.out.println("生成时间：" + (end - start));
        System.out.println("平均生成时间：" + new BigDecimal(end - start).divide(new BigDecimal(1000)));
    }


    @Test
    public void testMakeSnRandom() {
        String s = IdUtils.makeSnRandom();
        System.out.println(s);
        Assertions.assertEquals(20, s.length());
    }


}
