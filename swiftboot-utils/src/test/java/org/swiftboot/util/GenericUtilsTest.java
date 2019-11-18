package org.swiftboot.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @see GenericUtils
 */
public class GenericUtilsTest {

    @Test
    public void testParentsParent() {
        Assertions.assertTrue(LinkedList.class == GenericUtils.genericClass(CCC.class));
    }

    @Test
    public void testParent() {
        Assertions.assertTrue(LinkedList.class == GenericUtils.genericClass(BBB.class));
    }

    @Test
    public void testParentNoGenericType() {
        Assertions.assertThrows(RuntimeException.class, () -> GenericUtils.genericClass(AAA.class));
    }

    @Test
    public void testJavaObject() {
        Assertions.assertThrows(RuntimeException.class, () -> GenericUtils.genericClass(Object.class));
    }

    @Test
    public void testAncestor() {
        Assertions.assertThrows(RuntimeException.class, () -> GenericUtils.genericClass(DDD.class));
    }

    public static class AAA<T extends List> {
        public void foobar(T t) {
            System.out.println(t.getClass());
        }
    }

    public static class BBB extends AAA<LinkedList> {
        @Override
        public void foobar(LinkedList linkedList) {
            super.foobar(linkedList);
        }
    }

    public static class CCC extends BBB {
        @Override
        public void foobar(LinkedList linkedList) {
            super.foobar(linkedList);
        }
    }

    public static class DDD extends CCC {
        @Override
        public void foobar(LinkedList linkedList) {
            super.foobar(linkedList);
        }
    }
}

