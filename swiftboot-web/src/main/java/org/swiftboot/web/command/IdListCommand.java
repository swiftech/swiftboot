package org.swiftboot.web.command;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 业务对象 ID 列表
 *
 * @author swiftech
 */
@ApiModel
public class IdListCommand extends HttpCommand {

    @ApiModelProperty("业务对象ID列表")
    @JsonProperty("ids")
    @NotEmpty
    private List<@NotBlank @Length(min = 32, max = 32) String> ids;

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }
}