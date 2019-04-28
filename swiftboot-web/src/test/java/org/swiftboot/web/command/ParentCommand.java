package org.swiftboot.web.command;

import org.swiftboot.web.model.entity.ParentEntity;

import java.util.Set;

/**
 * @author Allen 2019-04-28
 **/
public class ParentCommand extends BasePopulateCommand<ParentEntity> {

    private String name;

    private Set<ChildCommand> items;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<ChildCommand> getItems() {
        return items;
    }

    public void setItems(Set<ChildCommand> items) {
        this.items = items;
    }
}
