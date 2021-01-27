package org.swiftboot.web.command;

import org.swiftboot.data.model.entity.ChildEntity;

/**
 * @author swiftech
 **/
public class ChildCommand extends BasePopulateCommand<ChildEntity> {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
