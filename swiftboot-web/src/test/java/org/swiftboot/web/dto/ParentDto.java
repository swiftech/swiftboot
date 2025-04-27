package org.swiftboot.web.dto;

import org.swiftboot.data.model.entity.ParentEntity;

import java.util.List;

/**
 * @author swiftech
 **/
public class ParentDto extends BasePopulateDto<ParentEntity> {

    private String name;

    private List<ChildDto> items;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ChildDto> getItems() {
        return items;
    }

    public void setItems(List<ChildDto> items) {
        this.items = items;
    }
}
