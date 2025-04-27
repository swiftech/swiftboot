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
        ParentDto result = new ParentDto();
        result.populateByEntity(entity);

        System.out.println(result.getName());
        Assertions.assertEquals(EntityMocker.PARENT_NAME, result.getName());

        int i = 0;
        for (ChildDto item : result.getItems()) {
            System.out.println(item.getName());
            Assertions.assertEquals(EntityMocker.CHILD_NAMES[i], item.getName());
            i++;
        }

    }
}
