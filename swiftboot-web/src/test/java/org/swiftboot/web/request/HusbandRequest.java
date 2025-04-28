package org.swiftboot.web.request;

import org.swiftboot.data.model.entity.HusbandEntity;

/**
 * @author swiftech
 **/
public class HusbandRequest extends BasePopulateRequest<HusbandEntity> {

    private String name;

    private WifeRequest wife;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public WifeRequest getWife() {
        return wife;
    }

    public void setWife(WifeRequest wife) {
        this.wife = wife;
    }
}
