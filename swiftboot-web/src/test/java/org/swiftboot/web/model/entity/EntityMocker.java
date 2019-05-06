package org.swiftboot.web.model.entity;

import java.util.ArrayList;

/**
 * 模拟复杂的实体类
 *
 * @author swiftech
 **/
public class EntityMocker {

    public static final String PARENT_NAME = "Parent Entity";
    public static final String[] CHILD_NAMES = new String[]{
            "Child Entity 1",
            "Child Entity 2"
    };

    public static ParentEntity mockParentEntity() {
        ParentEntity ret = new ParentEntity();
        ret.setName(PARENT_NAME);
        ChildEntity child1 = new ChildEntity();
        ChildEntity child2 = new ChildEntity();
        child1.setName(CHILD_NAMES[0]);
        child2.setName(CHILD_NAMES[1]);
        ret.setItems(new ArrayList<ChildEntity>() {
            {
                add(child1);
                add(child2);
            }
        });
        return ret;
    }
}
