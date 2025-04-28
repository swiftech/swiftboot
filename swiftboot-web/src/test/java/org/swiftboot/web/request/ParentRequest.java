package org.swiftboot.web.request;

import org.swiftboot.data.model.entity.ParentEntity;

import java.util.Set;

/**
 * @author swiftech
 **/
public class ParentRequest extends BasePopulateRequest<ParentEntity> {

    private String name;

    private Set<ChildRequest> items;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<ChildRequest> getItems() {
        return items;
    }

    public void setItems(Set<ChildRequest> items) {
        this.items = items;
    }
}
