package org.swiftboot.web.request;

import io.swagger.v3.oas.annotations.media.Schema;
import org.swiftboot.data.model.entity.OrphanEntity;

/**
 * @author swiftech
 **/
@Schema(description = "")
public class OrphanRequest extends BasePopulateRequest<OrphanEntity> {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
