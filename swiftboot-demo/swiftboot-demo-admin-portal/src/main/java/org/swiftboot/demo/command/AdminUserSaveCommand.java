package org.swiftboot.demo.command;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * 保存管理员
 *
 * @author swiftech 2020-01-06
 **/
@ApiModel
public class AdminUserSaveCommand extends AdminUserCreateCommand {

    @ApiModelProperty(value = "唯一标识", example = "441a3c4cbe574f17b2a3dc3fb5cda1c4")
    @NotBlank
    @Length(min = 32, max = 32)
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
     *
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }
}
