package org.swiftboot.web.command;

import io.swagger.annotations.ApiModel;
import org.swiftboot.data.model.entity.OrphanEntity;

/**
 * @author swiftech
 **/
@ApiModel
public class OrphanCommand extends BasePopulateCommand<OrphanEntity> {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
