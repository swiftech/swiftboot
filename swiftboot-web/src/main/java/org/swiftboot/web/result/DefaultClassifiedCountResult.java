package org.swiftboot.web.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.HashMap;
import java.util.Map;

/**
 * @author swiftech
 **/
@ApiModel("默认的按分类统计结果")
public class DefaultClassifiedCountResult<K, V> {

    @ApiModelProperty(value = "统计结果，按照分类的标识存储")
    @JsonProperty("count_map")
    private Map<K, V> map = new HashMap<>();

    public Map<K, V> getMap() {
        return map;
    }

    public void setMap(Map<K, V> map) {
        this.map = map;
    }
}
