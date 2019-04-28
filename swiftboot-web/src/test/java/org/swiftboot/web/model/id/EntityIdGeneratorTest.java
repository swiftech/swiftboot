package org.swiftboot.web.model.id;

import org.junit.jupiter.api.Test;
import org.swiftboot.web.model.id.entity.*;

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
