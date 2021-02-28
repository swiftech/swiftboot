package org.swiftboot.data.model.entity;

import org.swiftboot.data.annotation.PropertyDescription;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

/**
 * 所有数据库实体类基类，包括创建时间和事务锁版本
 *
 * @author swiftech
 */
@MappedSuperclass
public abstract class BaseTxVersionEntity extends BaseIdEntity implements TrxVersionPersistable{

    public BaseTxVersionEntity() {
    }

    /**
     * 事务锁版本
     */
    @Version
    @PropertyDescription(value = "Version for optimistic lock transaction", example = "1")
    @Column(name = "VERSION")
    private Long txVersion = 0L;

    public Long getTxVersion() {
        return txVersion == null ? 0 : txVersion;
    }

    public void setTxVersion(Long version) {
        this.txVersion = version;
    }
}
