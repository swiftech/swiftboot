package org.swiftboot.web.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author swiftech
 **/
@ApiModel("默认的统计结果")
public class DefaultCountResult {

    @ApiModelProperty(value = "统计结果")
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
