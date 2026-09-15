package org.swiftboot.demo.dto;

import org.swiftboot.data.model.entity.IdPersistable;
import org.swiftboot.web.dto.BasePopulateDto;

import java.time.LocalDateTime;

public class BaseCreateUpdateTimeDto<T extends IdPersistable> extends BasePopulateDto<T> {

    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
}
