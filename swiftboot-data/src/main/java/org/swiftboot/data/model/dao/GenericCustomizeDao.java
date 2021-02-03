package org.swiftboot.data.model.dao;

import org.swiftboot.data.model.entity.BaseIdEntity;

/**
 * @author swiftech
 */
public interface GenericCustomizeDao<T extends BaseIdEntity> {

    void saveEntity(T t);
}
