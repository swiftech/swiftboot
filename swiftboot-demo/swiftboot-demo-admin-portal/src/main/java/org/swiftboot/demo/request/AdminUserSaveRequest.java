package org.swiftboot.demo.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;

/**
 * 保存管理员
 *
 * @author swiftech 2020-01-06
 **/
@Schema
public class AdminUserSaveRequest extends AdminUserCreateRequest {

    @Schema(description = "唯一标识", example = "441a3c4cbe574f17b2a3dc3fb5cda1c4")
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
