package org.swiftboot.web.request;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.swiftboot.util.JsonUtils;
import org.swiftboot.data.model.entity.ChildEntity;
import org.swiftboot.data.model.entity.HusbandEntity;
import org.swiftboot.data.model.entity.OrphanEntity;
import org.swiftboot.data.model.entity.ParentEntity;

import java.util.HashSet;

/**
 * @author swiftech 2019-04-22
 **/
public class PopulateRequestTest {

    public static final String ORPHAN = "orphan";

    public static final String PARENT = "parent";

    public static final String CHILD = "child";

    public static final String HUSBAND = "husband";

    public static final String WIFE = "wife";

    @Test
    public void testSinglePopulate() {
        OrphanRequest cmd = new OrphanRequest();
        cmd.setName(ORPHAN);
        OrphanEntity entity = cmd.createEntity();
        Assertions.assertEquals(ORPHAN, entity.getName());
        System.out.println(JsonUtils.object2PrettyJson(entity));
    }

    @Test
    public void testFamilyPopulate() {
        ParentRequest cmd = new ParentRequest();
        cmd.setName(PARENT);
        cmd.setItems(new HashSet<>());
        ChildRequest subCmd = new ChildRequest();
        subCmd.setName(CHILD);
        cmd.getItems().add(subCmd);
        ParentEntity entity = cmd.createEntity();
        // assert
        Assertions.assertEquals(PARENT, entity.getName());
        if (entity.getItems() != null) {
            for (ChildEntity item : entity.getItems()) {
                Assertions.assertEquals(CHILD, item.getName());
            }
        }
        System.out.println(JsonUtils.object2PrettyJson(entity));
    }

    @Test
    public void testCouplePopulate() {
        HusbandRequest cmd = new HusbandRequest();
        cmd.setName(HUSBAND);
        WifeRequest wifeCmd = new WifeRequest();
        wifeCmd.setName(WIFE);
        cmd.setWife(wifeCmd);
        HusbandEntity entity = cmd.createEntity();
        Assertions.assertEquals(HUSBAND, entity.getName());
        Assertions.assertEquals(WIFE, entity.getWife().getName());
        System.out.println(JsonUtils.object2PrettyJson(entity));
    }
}
