package org.swiftboot.data.id;

import org.junit.jupiter.api.Test;
import org.swiftboot.data.id.entity.*;
import org.swiftboot.data.model.id.EntityIdGenerator;

/**
 * @author swiftech
 **/
public class EntityIdGeneratorTest {

    @Test
    public void testMakeUUIDByObjectAndServerId() {
        EntityIdGenerator entityIdGenerator = new EntityIdGenerator();
        String id = entityIdGenerator.generate(new AaaaBbbbCcccDdddEeeeEntity());
        System.out.println(id);
        id = entityIdGenerator.generate(new AaaaBbbbCcccDdddEntity());
        System.out.println(id);
        id = entityIdGenerator.generate(new AaaaBbbbCcccEntity());
        System.out.println(id);
        id = entityIdGenerator.generate(new AaaaBbbbEntity());
        System.out.println(id);
        id = entityIdGenerator.generate(new AaaaEntity());
        System.out.println(id);
        id = entityIdGenerator.generate(new AaaaEntity());
        System.out.println(id);
        id = entityIdGenerator.generate(new AEntity());
        System.out.println(id);
    }

}
