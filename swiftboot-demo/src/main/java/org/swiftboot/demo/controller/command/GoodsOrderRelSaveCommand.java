package org.swiftboot.demo.controller.command;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 保存商品订单关系
 *
 * @author swiftech 2019-04-07
 **/
@ApiModel
public class GoodsOrderRelSaveCommand extends GoodsOrderRelCreateCommand {

    @ApiModelProperty(value = "唯一标识", example = "441a3c4cbe574f17b2a3dc3fb5cda1c4")
    @NotBlank
    @Size(min = 32, max = 32)
    @JsonProperty("id")
    private String id;

    /**
     * 获取 ID
     *
     * @return
     */
    public String getId() {
        return id;
    }

    /**
     * 设置 ID
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }
}
