package org.swiftboot.web.command;

import org.swiftboot.web.model.entity.WifeEntity;

import javax.persistence.*;

/**
 * @author swiftech
 **/
public class WifeCommand extends BasePopulateCommand<WifeEntity> {

    private HusbandCommand parent;

    @Column
    private String name;

    public HusbandCommand getParent() {
        return parent;
    }

    public void setParent(HusbandCommand parent) {
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
