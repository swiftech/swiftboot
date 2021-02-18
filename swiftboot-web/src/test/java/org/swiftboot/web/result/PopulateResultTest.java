package org.swiftboot.web.result;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.swiftboot.data.model.entity.EntityMocker;
import org.swiftboot.data.model.entity.ParentEntity;

/**
 * @author swiftech
 **/
public class PopulateResultTest {

    @Test
    public void testPopulate() {
        ParentEntity entity = EntityMocker.mockParentEntity();
        ParentResult result = new ParentResult();
        result.populateByEntity(entity);

        System.out.println(result.getName());
        Assertions.assertEquals(EntityMocker.PARENT_NAME, result.getName());

        int i = 0;
        for (ChildResult item : result.getItems()) {
            System.out.println(item.getName());
            Assertions.assertEquals(EntityMocker.CHILD_NAMES[i], item.getName());
            i++;
        }

    }
}
