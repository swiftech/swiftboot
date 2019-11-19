package org.swiftboot.web.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 默认的统计数量返回对象
 *
 * @author swiftech
 **/
@ApiModel("Default counting result")
public class DefaultCountResult implements Result{

    /**
     * 统计结果
     */
    @ApiModelProperty(value = "Counting result")
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
