package org.swiftboot.web.command;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;


/**
 * 只包含单个业务 ID
 *
 * @author swiftech
 */
@ApiModel
public class IdCommand extends HttpCommand {

    /**
     * 业务对象ID
     */
    @ApiModelProperty("Business object ID")
    @JsonProperty("id")
    @NotBlank
    @Length(min = 32, max = 32)
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}