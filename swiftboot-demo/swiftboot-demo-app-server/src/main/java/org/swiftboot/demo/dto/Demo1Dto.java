package org.swiftboot.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Demo DTO 1")
public class Demo1Dto {

    @Schema(description = "content 1")
    private String content;

    @Schema(description = "value 1")
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
