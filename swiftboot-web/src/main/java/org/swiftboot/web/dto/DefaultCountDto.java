package org.swiftboot.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 默认的统计数量返回对象
 *
 * @author swiftech
 **/
@Schema(description = "Default counting result DTO")
public class DefaultCountDto implements Dto {

    /**
     * 统计结果
     */
    @Schema(description = "Counting result")
    private long count;

    public DefaultCountDto() {
    }

    public DefaultCountDto(long count) {
        this.count = count;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
