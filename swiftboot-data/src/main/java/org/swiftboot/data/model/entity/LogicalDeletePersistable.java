package org.swiftboot.data.model.entity;

import java.io.Serializable;

/**
 * 具有逻辑删除字段的可持久化对象
 *
 * @author swiftech
 * @since 2.1.0
 */
public interface LogicalDeletePersistable<T> extends Serializable {

    /**
     * 获取记录是否逻辑删除
     *
     * @return
     */
    T getIsDelete();

    /**
     * 设置记录为逻辑删除
     */
    void setIsDelete(T isDelete);
}
