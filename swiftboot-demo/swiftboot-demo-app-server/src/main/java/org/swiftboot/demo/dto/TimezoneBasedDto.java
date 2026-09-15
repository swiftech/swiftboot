package org.swiftboot.demo.dto;

import org.swiftboot.demo.model.TimezoneBasedEntity;
import org.swiftboot.web.dto.BasePopulateDto;

import java.time.Instant;

public class TimezoneBasedDto extends BasePopulateDto<TimezoneBasedEntity> {

    private Instant createTime;

    private Instant updateTime;

    private String name;

    public Instant getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Instant createTime) {
        this.createTime = createTime;
    }

    public Instant getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Instant updateTime) {
        this.updateTime = updateTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
