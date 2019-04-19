package org.swiftboot.web.exception;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.swiftboot.web.model.entity.ChildEntity;
import org.swiftboot.web.model.entity.ParentEntity;
import org.swiftboot.web.result.BasePopulateResult;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Allen 2019-03-18
 **/
public class BasePopulateResultTest {

    @Test
    public void test() {

        BasePopulateResult<ParentEntity> result = new TestResult();

        ParentEntity entity = new ParentEntity();
        entity.setName("测试实体类");
        ChildEntity entityItem1 = new ChildEntity();
        ChildEntity entityItem2 = new ChildEntity();
        entityItem1.setName("实体类子项1");
        entityItem2.setName("实体类子项2");
        entity.getItems().add(entityItem1);
        entity.getItems().add(entityItem2);
        // populate
        ((TestResult) result).setItems(new HashSet<>());// 初始化空的集合
        result.populateByEntity(entity);
        System.out.println(entity.getName());
        Assertions.assertFalse(StringUtils.isBlank(entity.getName()));
        for (TestItemResult item : ((TestResult) result).getItems()) {
            System.out.println(item.getName());
            Assertions.assertFalse(StringUtils.isBlank(item.getName()));
        }
    }

    public static class TestResult extends BasePopulateResult<ParentEntity> {
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

    public static class TestItemResult extends BasePopulateResult<ChildEntity> {
        String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
