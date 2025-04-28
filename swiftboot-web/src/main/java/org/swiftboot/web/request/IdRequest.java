package org.swiftboot.web.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;


/**
 * 只包含单个业务 ID
 *
 * @author swiftech
 */
@Schema(name="业务ID请求 Request with only ID")
public class IdRequest extends HttpRequest {

    /**
     * 业务对象ID
     */
    @Schema(description="Business object ID")
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