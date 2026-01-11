package org.swiftboot.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.HashMap;
import java.util.Map;

/**
 * 默认的按分类统计结果
 *
 * @author swiftech
 **/
@Schema(description="Default classified counting DTO")
public class DefaultClassifiedCountDto<K, V> implements Dto {

    /**
     * 统计结果，按照分类的标识存储
     *
     */
    @Schema(description = "Counting results map")
    private Map<K, V> countMap = new HashMap<>();

    public Map<K, V> getCountMap() {
        return countMap;
    }

    public void setCountMap(Map<K, V> countMap) {
        this.countMap = countMap;
    }
}
