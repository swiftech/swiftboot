package org.swiftboot.web.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.swiftboot.data.model.entity.ChildEntity;
import org.swiftboot.data.model.entity.EntityMocker;
import org.swiftboot.data.model.entity.ParentEntity;

/**
 * @author swiftech
 **/
public class PopulateDtoTest {


    @Test
    public void testPopulateParent() {
        ParentEntity entity = EntityMocker.mockParentEntity(true);
        ParentDto dto = new ParentDto();
        dto.populateByEntity(entity);

        System.out.println(dto.getName());
        Assertions.assertEquals(EntityMocker.PARENT_NAME, dto.getName());

        int i = 0;
        for (ChildDto item : dto.getItems()) {
            System.out.println(item.getName());
            Assertions.assertEquals(EntityMocker.CHILD_NAMES[i], item.getName());
            Assertions.assertNull(item.getParent()); // 因无限循环而中止
            if (i == 0) {
                Assertions.assertNotNull(item.getToy()); // 非循环的多层关联关系正常处理
            }
            else {
                Assertions.assertNull(item.getToy());
            }
            i++;
        }
    }

    @Test
    public void testPopulateChild() {
        ChildEntity entity = EntityMocker.mockChildEntity(true);
        ChildDto dto = new ChildDto();
        dto.populateByEntity(entity);

        System.out.println(dto.getName());

        Assertions.assertEquals(EntityMocker.CHILD_NAMES[0], dto.getName());

        Assertions.assertEquals(EntityMocker.PARENT_NAME, dto.getParent().getName());

        Assertions.assertNull(dto.getParent().getItems()); // 因无限循环而中止

        Assertions.assertNotNull(dto.getToy());

    }

    /**
     * 测试不包含子对象
     */
    @Test
    public void testPopulateParentNotIncludeChildren() {
        ParentEntity entity = EntityMocker.mockParentEntity(true);
        ParentDto dto = new ParentDto();
        dto.populateByEntity(entity, false);

        System.out.println(dto.getName());
        Assertions.assertEquals(EntityMocker.PARENT_NAME, dto.getName());

        Assertions.assertNull(dto.getItems());

    }

    /**
     * 测试不包含父对象
     */
    @Test
    public void testPopulateChildNotIncludeParent() {
        ChildEntity entity = EntityMocker.mockChildEntity(true);
        ChildDto dto = new ChildDto();
        dto.populateByEntity(entity, false);

        System.out.println(dto.getName());
        Assertions.assertEquals(EntityMocker.CHILD_NAMES[0], dto.getName());

        Assertions.assertNull(dto.getParent());

        Assertions.assertNull(dto.getToy());
    }
}
