package org.swiftboot.web.request;

import org.swiftboot.data.model.entity.ChildEntity;

/**
 * @author swiftech
 **/
public class ChildRequest extends BasePopulateRequest<ChildEntity> {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
