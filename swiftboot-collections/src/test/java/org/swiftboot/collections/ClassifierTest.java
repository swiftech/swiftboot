package org.swiftboot.collections;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.swiftboot.collections.CollectionUtilsTest.TestBean;

import java.util.*;

/**
 * @author swiftech
 */
public class ClassifierTest {
    List<TestBean> list = null;

    @BeforeEach
    public void setup() {
        list = new ArrayList<TestBean>() {
            {
                add(new TestBean("a", "a3"));
                add(new TestBean("a", "a1"));
                add(new TestBean("a", "a2"));
                add(new TestBean("c", "c3"));
                add(new TestBean("c", "c2"));
                add(new TestBean("c", "c1"));
                add(new TestBean("b", "b2"));
                add(new TestBean("b", "b1"));
                add(new TestBean("b", "b3"));
            }
        };
    }

    @Test
    public void testSimple() {
        Classifier<String, TestBean> classifier = new ClassifierBuilder<String, TestBean>()
                .trait(TestBean::getKey)
                .createClassifier();
        Map<String, Collection<TestBean>> result = classifier.classify(list);
        System.out.println(result);
    }

    @Test
    public void testWithList() {
        Classifier<String, TestBean> classifier = new ClassifierBuilder<String, TestBean>()
                .trait(TestBean::getKey)
                .traitComparator(String::compareTo)
                .collectionComparator(Comparator.comparing(TestBean::getValue))
                .createClassifier();

        Map<String, Collection<TestBean>> result = classifier.classify(list);

        this.assertResult(result);

    }


    @Test
    public void testWithSet() {

        Classifier<String, TestBean> classifier = new ClassifierBuilder<String, TestBean>()
                .trait(TestBean::getKey)
                .traitComparator(Comparator.naturalOrder())
                .collectionComparator(Comparator.comparing(TestBean::getValue))
                .createClassifier();

        Map<String, Collection<TestBean>> result = classifier.classify(list);

        this.assertResult(result);

    }

    private void assertResult(Map<String, Collection<TestBean>> result) {

        for (Object k : result.keySet()) {
            System.out.println(k);
            System.out.println(result.get(k));
        }
        Collection<TestBean> ca = result.get("a");
        Assertions.assertNotNull(ca);
        Assertions.assertEquals(3, ca.size());
        for (TestBean e : ca) {
            Assertions.assertEquals("a", e.getValue().substring(0, 1));
        }

        Collection<TestBean> cb = result.get("b");
        Assertions.assertNotNull(cb);
        Assertions.assertEquals(3, cb.size());
        for (TestBean e : cb) {
            Assertions.assertEquals("b", e.getValue().substring(0, 1));
        }
    }
}
