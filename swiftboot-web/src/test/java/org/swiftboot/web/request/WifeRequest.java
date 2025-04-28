package org.swiftboot.web.request;

import org.swiftboot.data.model.entity.WifeEntity;

import jakarta.persistence.*;

/**
 * @author swiftech
 **/
public class WifeRequest extends BasePopulateRequest<WifeEntity> {

    private HusbandRequest parent;

    @Column
    private String name;

    public HusbandRequest getParent() {
        return parent;
    }

    public void setParent(HusbandRequest parent) {
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
