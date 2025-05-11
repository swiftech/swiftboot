package org.swiftboot.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.swiftboot.data.model.entity.ChildEntity;
import org.swiftboot.data.model.entity.ToyEntity;

@Schema
public class ToyDto extends BasePopulateDto<ToyEntity> {

    private String name;

    private ChildEntity child;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ChildEntity getChild() {
        return child;
    }

    public void setChild(ChildEntity child) {
        this.child = child;
    }
}
