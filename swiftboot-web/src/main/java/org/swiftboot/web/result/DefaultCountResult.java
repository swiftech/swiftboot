package org.swiftboot.web.result;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 默认的统计数量返回对象
 *
 * @author swiftech
 **/
@Schema(name="", description="Default counting result")
public class DefaultCountResult implements Result{

    /**
     * 统计结果
     */
    @Schema(description = "Counting result")
    private long count;

    public DefaultCountResult() {
    }

    public DefaultCountResult(long count) {
        this.count = count;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
