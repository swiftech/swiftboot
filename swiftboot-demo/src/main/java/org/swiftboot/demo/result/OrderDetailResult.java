package org.swiftboot.demo.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.swiftboot.web.result.BasePopulateResult;

/**
 * @author Allen 2019-04-18
 **/
public class OrderDetailResult extends BasePopulateResult {

    private String id;

    @ApiModelProperty(value = "订单明细", example = "xxxxxx")
    @JsonProperty("description")
    private String description;

    public OrderDetailResult() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
