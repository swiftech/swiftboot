package org.swiftboot.web.dto;

import org.swiftboot.data.model.entity.ParentEntity;

/**
 * @author swiftech
 **/
public class ChildDto extends BasePopulateDto<ParentEntity> {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
