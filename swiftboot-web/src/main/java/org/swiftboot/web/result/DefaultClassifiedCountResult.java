package org.swiftboot.web.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.HashMap;
import java.util.Map;

/**
 * 默认的按分类统计结果
 *
 * @author swiftech
 **/
@Schema(name="", description="Default classified counting result")
public class DefaultClassifiedCountResult<K, V> implements Result{

    /**
     * 统计结果，按照分类的标识存储
     *
     */
    @Schema(description = "Counting results map")
    @JsonProperty("count_map")
    private Map<K, V> map = new HashMap<>();

    public Map<K, V> getMap() {
        return map;
    }

    public void setMap(Map<K, V> map) {
        this.map = map;
    }
}
