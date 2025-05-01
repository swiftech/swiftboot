package org.swiftboot.data.repository;

import org.swiftboot.data.model.entity.BaseIdEntity;

/**
 * @author swiftech
 */
public interface GenericCustomizeRepository<T extends BaseIdEntity> {

    void saveEntity(T t);
}
