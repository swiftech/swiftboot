package org.swiftboot.web.command;

import org.swiftboot.data.model.entity.HusbandEntity;

/**
 * @author swiftech
 **/
public class HusbandCommand extends BasePopulateCommand<HusbandEntity> {

    private String name;

    private WifeCommand wife;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public WifeCommand getWife() {
        return wife;
    }

    public void setWife(WifeCommand wife) {
        this.wife = wife;
    }
}
