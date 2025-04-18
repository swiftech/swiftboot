package org.swiftboot.web.command;

import io.swagger.v3.oas.annotations.media.Schema;
import org.swiftboot.data.model.entity.OrphanEntity;

/**
 * @author swiftech
 **/
@Schema(name="", description = "")
public class OrphanCommand extends BasePopulateCommand<OrphanEntity> {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
