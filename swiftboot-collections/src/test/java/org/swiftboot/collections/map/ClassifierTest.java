package org.swiftboot.collections.map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.swiftboot.collections.Classifier;
import org.swiftboot.collections.ClassifierBuilder;
import org.swiftboot.collections.map.CollectionUtilsTest.TestBean;

import java.util.*;

/**
 * allen
 */
public class ClassifierTest {


    @Test
    public void testWithList() {
        List<TestBean> list = new ArrayList<TestBean>() {
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

        Classifier<String, TestBean> classifier = new ClassifierBuilder<String, TestBean>()
                .setTrait(TestBean::getKey)
                .setTraitComparator((Comparator<String>) (o1, o2) -> o1.compareTo(o2))
                .setCollectionComparator((Comparator<TestBean>) (o1, o2) -> o1.getValue().compareTo(o2.getValue()))
                .createClassifier();

        Map<String, Collection<TestBean>> result = classifier.classify(list);

        this.assertResult(result);

    }


    @Test
    public void testWithSet() {
        Set<TestBean> list = new HashSet<TestBean>() {
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

        Classifier<String, TestBean> classifier = new ClassifierBuilder<String, TestBean>()
                .setTrait(TestBean::getKey)
                .setTraitComparator((Comparator<String>) (o1, o2) -> o1.compareTo(o2))
                .setCollectionComparator((Comparator<TestBean>) (o1, o2) -> o1.getValue().compareTo(o2.getValue()))
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
