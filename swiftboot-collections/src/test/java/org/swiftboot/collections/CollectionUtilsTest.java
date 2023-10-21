package org.swiftboot.collections;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

/**
 * @author swiftech 2019-04-28
 **/
public class CollectionUtilsTest {

    public static void main(String[] args) {
        List<String> l = new ArrayList<String>() {
            {
                add("hello");
                add("list");
            }
        };
        String[] objects = l.toArray(new String[]{});
        for (String object : objects) {
            System.out.println(object);
        }

    }

    @Test
    public void toLinkedList() {
        String[] a = new String[]{"a", "b", "c"};
        LinkedList<Object> linkedList = CollectionUtils.toLinkedList(a);
        Assertions.assertNotNull(linkedList);
        Assertions.assertEquals(3, linkedList.size());

        LinkedList<Object> linkedList1 = CollectionUtils.toLinkedList("a", "b", "c");
        Assertions.assertNotNull(linkedList1);
        Assertions.assertEquals(3, linkedList1.size());
    }

    @Test
    public void testContains() {
        List<Object> list = Arrays.asList("a", new TestBean("b", "v"));
        Assertions.assertTrue(CollectionUtils.contains(list, TestBean.class));
    }

    @Test
    public void testFirstMatch() {
        TestBean b = new TestBean("b", "v");
        List<Object> list = Arrays.asList("a", b);
        Assertions.assertEquals(b, CollectionUtils.getFirstMatch(list, TestBean.class));
        Assertions.assertEquals(b, CollectionUtils.getFirstMatch(list.toArray(), TestBean.class));
    }

    @Test
    public void testConstructByType() {
        Assertions.assertDoesNotThrow(() -> {
            List<?> list = CollectionUtils.constructCollectionByType(List.class);
            Assertions.assertNotNull(list);

            Set<?> set = CollectionUtils.constructCollectionByType(Set.class);
            Assertions.assertNotNull(set);
        });
    }

    @Test
    public void testSortListCollection() {
        List<TestBean> list = new ArrayList<TestBean>() {
            {
                add(new TestBean("b", "b3"));
                add(new TestBean("b", "b1"));
                add(new TestBean("b", "b2"));
                add(new TestBean("a", "a2"));
                add(new TestBean("a", "a1"));
                add(new TestBean("a", "a3"));
            }
        };
        Collection<TestBean> testBeans = CollectionUtils.sortCollection(list, Comparator.comparing(TestBean::getValue));
        for (TestBean testBean : testBeans) {
            System.out.println(testBean);
        }
    }

    @Test
    public void testSortHashSetCollection() {
        Set<TestBean> list = new HashSet<TestBean>() {
            {
                add(new TestBean("b", "b3"));
                add(new TestBean("b", "b1"));
                add(new TestBean("b", "b2"));
                add(new TestBean("a", "a2"));
                add(new TestBean("a", "a1"));
                add(new TestBean("a", "a3"));
            }
        };
        Collection<TestBean> testBeans = CollectionUtils.sortCollection(list, Comparator.comparing(TestBean::getValue));
        for (TestBean testBean : testBeans) {
            System.out.println(testBean);
        }
    }

    @Test
    public void testSortTreeSetCollection() {
        Set<TestBean> list = new TreeSet<TestBean>(Comparator.comparing(TestBean::getValue)) {
            {
                add(new TestBean("b", "b3"));
                add(new TestBean("b", "b1"));
                add(new TestBean("b", "b2"));
                add(new TestBean("a", "a2"));
                add(new TestBean("a", "a1"));
                add(new TestBean("a", "a3"));
            }
        };
        Collection<TestBean> testBeans = CollectionUtils.sortCollection(list, Comparator.comparing(TestBean::getValue));
        for (TestBean testBean : testBeans) {
            System.out.println(testBean);
        }
    }

    public static class TestBean {
        String key;
        String value;

        public TestBean(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "TestBean{" +
                    "key='" + key + '\'' +
                    ", value='" + value + '\'' +
                    '}';
        }

//        @Override
//        public int compareTo(TestBean o) {
//            return this.key.compareTo(o.getKey());
//        }
    }

    @Test
    public void hasDuplicate() {
        List<String> noDupList  = Arrays.asList("a", "b", "c");
        List<String> dupList  = Arrays.asList("a", "b", "a");
        Assertions.assertFalse(CollectionUtils.hasDuplicate(noDupList));
        Assertions.assertTrue(CollectionUtils.hasDuplicate(dupList));
    }
}
