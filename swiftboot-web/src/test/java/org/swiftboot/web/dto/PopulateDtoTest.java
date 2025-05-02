package org.swiftboot.web.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.swiftboot.data.model.entity.EntityMocker;
import org.swiftboot.data.model.entity.ParentEntity;

/**
 * @author swiftech
 **/
public class PopulateDtoTest {

    @Test
    public void testPopulate() {
        ParentEntity entity = EntityMocker.mockParentEntity();
        ParentDto dto = new ParentDto();
        dto.populateByEntity(entity);

        System.out.println(dto.getName());
        Assertions.assertEquals(EntityMocker.PARENT_NAME, dto.getName());

        int i = 0;
        for (ChildDto item : dto.getItems()) {
            System.out.println(item.getName());
            Assertions.assertEquals(EntityMocker.CHILD_NAMES[i], item.getName());
            i++;
        }

    }
}
