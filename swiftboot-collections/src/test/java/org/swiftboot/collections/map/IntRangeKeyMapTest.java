package org.swiftboot.collections.map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @since 2.4.8
 */
class IntRangeKeyMapTest {

    @Test
    void containsKey() {
        IntRangeKeyMap<Integer, String> map = new IntRangeKeyMap<>();
        map.put(1, 10, "r1");
        map.put(11, 20, "r2");
        Assertions.assertTrue(map.containsKey(1));
        Assertions.assertTrue(map.containsKey(5));
        Assertions.assertTrue(map.containsKey(10));
    }

    @Test
    void get() {
        IntRangeKeyMap<Integer, String> map = new IntRangeKeyMap<>();
        map.put(1, 10, "r1");
        map.put(11, 20, "r2");
        Assertions.assertEquals("r1", map.get(5));
        Assertions.assertEquals("r2", map.get(15));
    }

    @Test
    void put() {
    }

}