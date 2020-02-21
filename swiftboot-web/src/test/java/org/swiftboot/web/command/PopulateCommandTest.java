package org.swiftboot.web.command;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.swiftboot.util.JsonUtils;
import org.swiftboot.web.model.entity.ChildEntity;
import org.swiftboot.web.model.entity.HusbandEntity;
import org.swiftboot.web.model.entity.OrphanEntity;
import org.swiftboot.web.model.entity.ParentEntity;

import java.util.HashSet;

/**
 * @author swiftech 2019-04-22
 **/
public class PopulateCommandTest {

    public static final String ORPHAN = "orphan";

    public static final String PARENT = "parent";

    public static final String CHILD = "child";

    public static final String HUSBAND = "husband";

    public static final String WIFE = "wife";

    @Test
    public void testSinglePopulate() {
        OrphanCommand cmd = new OrphanCommand();
        cmd.setName(ORPHAN);
        OrphanEntity entity = cmd.createEntity();
        Assertions.assertEquals(ORPHAN, entity.getName());
        System.out.println(JsonUtils.object2PrettyJson(entity));
    }

    @Test
    public void testFamilyPopulate() {
        ParentCommand cmd = new ParentCommand();
        cmd.setName(PARENT);
        cmd.setItems(new HashSet<>());
        ChildCommand subCmd = new ChildCommand();
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
        HusbandCommand cmd = new HusbandCommand();
        cmd.setName(HUSBAND);
        WifeCommand wifeCmd = new WifeCommand();
        wifeCmd.setName(WIFE);
        cmd.setWife(wifeCmd);
        HusbandEntity entity = cmd.createEntity();
        Assertions.assertEquals(HUSBAND, entity.getName());
        Assertions.assertEquals(WIFE, entity.getWife().getName());
        System.out.println(JsonUtils.object2PrettyJson(entity));
    }
}
