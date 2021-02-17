package org.swiftboot.data.model.entity;

import java.io.Serializable;

/**
 * 具有事务锁版本号的可持久化对象
 *
 * @author swiftech
 * @since 2.0.0
 */
public interface TrxVersionPersistable extends Serializable {

    /**
     * 获取事务锁版本
     *
     * @return
     */
    Long getTxVersion();

    /**
     * 设置事务锁版本
     *
     * @param version
     */
    void setTxVersion(Long version);
}
