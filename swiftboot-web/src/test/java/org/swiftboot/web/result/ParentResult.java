package org.swiftboot.web.result;

import org.swiftboot.web.model.entity.ParentEntity;

import java.util.Set;

/**
 * @author swiftech
 **/
public class ParentResult extends BasePopulateResult<ParentEntity> {

    private String name;

    private Set<ChildResult> items;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<ChildResult> getItems() {
        return items;
    }

    public void setItems(Set<ChildResult> items) {
        this.items = items;
    }
}
