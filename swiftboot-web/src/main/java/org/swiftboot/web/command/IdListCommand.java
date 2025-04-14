package org.swiftboot.web.command;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.Length;

import java.util.List;

/**
 * 业务对象 ID 列表
 *
 * @author swiftech
 */
@Schema(name="Id List Command")
public class IdListCommand extends HttpCommand {

    /**
     * 业务对象ID列表
     */
    @Schema(description="List of business object ID")
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