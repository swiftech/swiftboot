package org.swiftboot.web.exception;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.swiftboot.data.model.entity.ChildEntity;
import org.swiftboot.data.model.entity.ParentEntity;
import org.swiftboot.web.dto.BasePopulateDto;

import java.util.HashSet;
import java.util.Set;

/**
 * @author swiftech 2019-03-18
 **/
public class BasePopulateDtoTest {

    @Test
    public void test() {

        TestDto dto = new TestDto();

        ParentEntity entity = new ParentEntity();
        entity.setName("测试实体类");
        ChildEntity entityItem1 = new ChildEntity();
        ChildEntity entityItem2 = new ChildEntity();
        entityItem1.setName("实体类子项1");
        entityItem2.setName("实体类子项2");
        entity.getItems().add(entityItem1);
        entity.getItems().add(entityItem2);
        // populate
        dto.setItems(new HashSet<>());// 初始化空的集合
        dto.populateByEntity(entity);
        System.out.println(entity.getName());
        Assertions.assertFalse(StringUtils.isBlank(entity.getName()));
        for (TestItemDto item : dto.getItems()) {
            System.out.println(item.getName());
            Assertions.assertFalse(StringUtils.isBlank(item.getName()));
        }
    }

    public static class TestDto extends BasePopulateDto<ParentEntity> {
        String name;

        Set<TestItemDto> items;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Set<TestItemDto> getItems() {
            return items;
        }

        public void setItems(Set<TestItemDto> items) {
            this.items = items;
        }
    }

    public static class TestItemDto extends BasePopulateDto<ChildEntity> {
        String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
