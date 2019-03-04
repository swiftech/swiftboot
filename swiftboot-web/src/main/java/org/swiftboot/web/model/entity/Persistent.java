package org.swiftboot.web.model.entity;

import java.io.Serializable;

/**
 * 可持久化对象
 * @author swiftech
 **/
public interface Persistent extends Serializable, IdPojo {

    /**
     * 获取持久化的时间
     * @return
     */
    Long getCreateTime();

    /**
     * 设置持久化的时间
     * @param createTime
     */
    void setCreateTime(Long createTime);

    /**
     * 获取修改的时间
     * @return
     */
    Long getUpdateTime();

    /**
     * 设置修改的时间
     * @param updateTime
     */
    void setUpdateTime(Long updateTime);
}
