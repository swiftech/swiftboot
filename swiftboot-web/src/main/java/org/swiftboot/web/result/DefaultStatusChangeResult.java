package org.swiftboot.web.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 默认的修改状态返回对象
 *
 * @author swiftech
 **/
@Schema(name="", description="Default status changing result")
public class DefaultStatusChangeResult {

    /**
     * 新状态码
     */
    @Schema(description = "New status code")
    @JsonProperty("new_status_code")
    private int newStatusCode;

    /**
     * 新状态名称
     */
    @Schema(description = "New status name")
    @JsonProperty("new_status_name")
    private String newStatusName;

    public DefaultStatusChangeResult(int newStatusCode) {
        this.newStatusCode = newStatusCode;
    }

    public DefaultStatusChangeResult(int newStatusCode, String newStatusName) {
        this.newStatusCode = newStatusCode;
        this.newStatusName = newStatusName;
    }

    public int getNewStatusCode() {
        return newStatusCode;
    }

    public void setNewStatusCode(int newStatusCode) {
        this.newStatusCode = newStatusCode;
    }

    public String getNewStatusName() {
        return newStatusName;
    }

    public void setNewStatusName(String newStatusName) {
        this.newStatusName = newStatusName;
    }
}
