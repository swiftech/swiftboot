package org.swiftboot.collections;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

/**
 * @author swiftech
 **/
public class ArrayUtilsTest {

    @Test
    public void getFirstMatch() {

        Object[] array = new Object[]{
                new ArrayList<String>() {
                    {
                        add("第一个ArrayList");
                    }
                },
                new HashMap<>(),
                new ArrayList<String>() {
                    {
                        add("第二个ArrayList");
                    }
                },
        };

        Object firstList = ArrayUtils.getFirstMatch(array, List.class);
        System.out.println(firstList);
        Assertions.assertTrue(firstList instanceof List);
        Assertions.assertTrue(firstList instanceof ArrayList);
        Assertions.assertEquals("第一个ArrayList", ((List) firstList).get(0));
        System.out.println(((List) firstList).get(0));


        Object firstMap = ArrayUtils.getFirstMatch(array, Map.class);
        Assertions.assertTrue(firstMap instanceof Map);
        System.out.println(firstMap);

        Object firstSet = ArrayUtils.getFirstMatch(array, Set.class);
        System.out.println(firstSet);
    }

    @Test
    void merge() {
        Assertions.assertNull(ArrayUtils.merge(null, null));
        Assertions.assertNotNull(ArrayUtils.merge(new int[]{}, null));
        Assertions.assertNotNull(ArrayUtils.merge(null, new int[]{}));
        Assertions.assertNotNull(ArrayUtils.merge(new int[]{}, new int[]{}));
        Assertions.assertEquals(0, ArrayUtils.merge(new int[]{}, new int[]{}).length);

        Assertions.assertArrayEquals(new int[]{1, 2, 3}, ArrayUtils.merge(new int[]{1, 2, 3}, new int[]{}));
        Assertions.assertArrayEquals(new int[]{1, 2, 3}, ArrayUtils.merge(new int[]{1, 2, 3}, null));
        Assertions.assertArrayEquals(new int[]{1, 2, 3, 4, 5}, ArrayUtils.merge(new int[]{1, 2, 3}, new int[]{4, 5}));
    }
}
