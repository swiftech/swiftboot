package org.swiftboot.collections.map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.swiftboot.collections.ClassifyFilter;
import org.swiftboot.collections.CollectionUtils;

import java.util.*;

/**
 * @author Allen 2019-04-28
 **/
public class CollectionUtilsTest {

    @Test
    public void testConstructByType() {
        Assertions.assertDoesNotThrow(new Executable() {
            @Override
            public void execute() {
                List list = CollectionUtils.constructCollectionByType(List.class);
                Assertions.assertNotNull(list);

                Set set = CollectionUtils.constructCollectionByType(Set.class);
                Assertions.assertNotNull(set);
            }
        });
    }

    @Test
    public void testClassify() {
        List<TestBean> list = new ArrayList<TestBean>() {
            {
                add(new TestBean("a", "a1"));
                add(new TestBean("a", "a2"));
                add(new TestBean("a", "a3"));
                add(new TestBean("b", "b1"));
                add(new TestBean("b", "b2"));
                add(new TestBean("b", "b3"));
            }
        };
        Map<Object, Collection> classify = CollectionUtils.classify(list, new ClassifyFilter<TestBean>() {
            @Override
            public String filter(TestBean testBean) {
                return testBean.getKey();
            }
        });
        for (Object k : classify.keySet()) {
            System.out.println(k);
            System.out.println(classify.get(k));
        }
        Collection<TestBean> ca = classify.get("a");
        Assertions.assertNotNull(ca);
        Assertions.assertEquals(3, ca.size());
        for (TestBean e : ca) {
            Assertions.assertEquals("a", e.getValue().substring(0, 1));
        }

        Collection<TestBean> cb = classify.get("b");
        Assertions.assertNotNull(cb);
        Assertions.assertEquals(3, cb.size());
        for (TestBean e : cb) {
            Assertions.assertEquals("b", e.getValue().substring(0, 1));
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
    }
}
