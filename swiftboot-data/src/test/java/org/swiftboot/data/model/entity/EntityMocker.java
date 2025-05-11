package org.swiftboot.data.model.entity;

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
    public static final String TOY_NAME = "Toy Entity";

    /**
     * @param attached if true, the parent entity will be linked to all child entities
     * @return
     */
    public static ParentEntity mockParentEntity(boolean attached) {
        ParentEntity ret = new ParentEntity();
        ChildEntity child1 = new ChildEntity();
        ChildEntity child2 = new ChildEntity();
        ToyEntity toy = new ToyEntity();
        ret.setName(PARENT_NAME);
        child1.setName(CHILD_NAMES[0]);
        child2.setName(CHILD_NAMES[1]);
        toy.setName(TOY_NAME);
        child1.setToy(toy);
        ret.setItems(new ArrayList<>() {
            {
                add(child1);
                add(child2);
            }
        });
        if (attached) {
            child1.setParent(ret);
            child2.setParent(ret);
        }
        return ret;
    }

    public static ChildEntity mockChildEntity(boolean attached) {
        ParentEntity parent = new ParentEntity();
        ChildEntity ret = new ChildEntity();
        ToyEntity toy = new ToyEntity();
        parent.setName(PARENT_NAME);
        ret.setName(CHILD_NAMES[0]);
        toy.setName(TOY_NAME);
        ret.setParent(parent);
        ret.setToy(toy);
        if (attached) {
            parent.setItems(new ArrayList<>() {
                {
                    add(ret);
                }
            });
        }
        return ret;
    }
}
