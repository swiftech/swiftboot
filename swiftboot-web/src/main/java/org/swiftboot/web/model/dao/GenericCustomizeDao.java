package org.swiftboot.web.model.dao;

import org.swiftboot.web.model.entity.BaseIdEntity;

/**
 * allen
 */
public interface GenericCustomizeDao<T extends BaseIdEntity> {

    void saveEntity(T t);
}
