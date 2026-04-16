package org.swiftboot.demo.request;

import io.swagger.v3.oas.annotations.media.Schema;
import org.hibernate.validator.constraints.Length;

/**
 * @since 3.1.1
 */
public class MyRequest {

    @Length(min = 1, max = 2)
    @Schema(description = "i8n.validation.my.param")
    private String myParam;

    @Length(min = 1, max = 2)
    @Schema(description = "i8n.validation.my.param2")
    private String myParam2;

    public String getMyParam() {
        return myParam;
    }

    public void setMyParam(String myParam) {
        this.myParam = myParam;
    }

    public String getMyParam2() {
        return myParam2;
    }

    public void setMyParam2(String myParam2) {
        this.myParam2 = myParam2;
    }
}
