package org.swiftboot.web.dto;

import org.swiftboot.data.model.entity.ChildEntity;

/**
 * @author swiftech
 **/
public class ChildDto extends BasePopulateDto<ChildEntity> {

    private String name;

    private ParentDto parent;

    private ToyDto toy;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ParentDto getParent() {
        return parent;
    }

    public void setParent(ParentDto parent) {
        this.parent = parent;
    }

    public ToyDto getToy() {
        return toy;
    }

    public void setToy(ToyDto toy) {
        this.toy = toy;
    }
}
