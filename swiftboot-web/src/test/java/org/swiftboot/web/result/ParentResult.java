package org.swiftboot.web.result;

import org.swiftboot.web.model.entity.ParentEntity;

import java.util.List;

/**
 * @author swiftech
 **/
public class ParentResult extends BasePopulateResult<ParentEntity> {

    private String name;

    private List<ChildResult> items;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ChildResult> getItems() {
        return items;
    }

    public void setItems(List<ChildResult> items) {
        this.items = items;
    }
}
