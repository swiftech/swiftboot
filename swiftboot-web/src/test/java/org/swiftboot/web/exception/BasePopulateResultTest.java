package org.swiftboot.web.exception;

import org.junit.jupiter.api.Test;
import org.swiftboot.web.model.entity.BaseEntity;
import org.swiftboot.web.result.BasePopulateResult;

import javax.persistence.Column;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Allen 2019-03-18
 **/
public class BasePopulateResultTest {

    @Test
    public void test() {

        BasePopulateResult<TestEntity> result = new TestResult();

        TestEntity entity = new TestEntity();
        entity.setName("测试实体类");
        TestItemEntity entityItem = new TestItemEntity();
        entity.getItems().add(entityItem);
        result.populateByEntity(entity);
        System.out.println(entity.getName());
    }

    public static class TestEntity extends BaseEntity {
        @Column
        String name;

        Set<TestItemEntity> items = new HashSet<>();

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Set<TestItemEntity> getItems() {
            return items;
        }

        public void setItems(Set<TestItemEntity> items) {
            this.items = items;
        }
    }

    public static class TestItemEntity extends BaseEntity {
        @Column
        String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class TestResult extends BasePopulateResult<TestEntity> {
        String name;

        Set<TestItemResult> items;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Set<TestItemResult> getItems() {
            return items;
        }

        public void setItems(Set<TestItemResult> items) {
            this.items = items;
        }
    }

    public static class TestItemResult extends BasePopulateResult<TestItemEntity> {
        String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
