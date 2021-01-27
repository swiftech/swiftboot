package org.swiftboot.web.result;

import org.swiftboot.data.model.entity.ParentEntity;

/**
 * @author swiftech
 **/
public class ChildResult extends BasePopulateResult<ParentEntity> {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
