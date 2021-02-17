package org.swiftboot.data.model.entity;

import java.io.Serializable;

/**
 * 具有创建时间和更新时间的可持久化对象
 *
 * @param <T> 表示时间的类型，例如 {@code java.lang.Long}, {@code java.util.Date}, {@code java.time.LocalDateTime}
 * @author swiftech
 */
public interface TimePersistable<T> extends Serializable, IdPersistable {

    /**
     * 获取持久化的时间
     *
     * @return
     */
    T getCreateTime();

    /**
     * 设置持久化的时间
     *
     * @param createTime
     */
    void setCreateTime(T createTime);

    /**
     * 获取修改的时间
     *
     * @return
     */
    T getUpdateTime();

    /**
     * 设置修改的时间
     *
     * @param updateTime
     */
    void setUpdateTime(T updateTime);
}
