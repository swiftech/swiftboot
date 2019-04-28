package org.swiftboot.web.command;

import io.swagger.annotations.ApiModel;
import org.swiftboot.web.model.entity.OrphanEntity;

/**
 * @author Allen 2019-04-22
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
