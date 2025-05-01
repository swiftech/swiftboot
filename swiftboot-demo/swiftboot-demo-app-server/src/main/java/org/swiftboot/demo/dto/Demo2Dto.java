package org.swiftboot.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Demo DTO 2")
public class Demo2Dto {

    @Schema(description = "content 2")
    private String content;

    @Schema(description = "value 2")
    private String value;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
