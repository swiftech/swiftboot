package org.swiftboot.web.command;

import org.swiftboot.web.model.entity.ChildEntity;

/**
 * @author Allen 2019-04-28
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
