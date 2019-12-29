package org.swiftboot.collections;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.swiftboot.collections.ArrayUtils;

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
}
