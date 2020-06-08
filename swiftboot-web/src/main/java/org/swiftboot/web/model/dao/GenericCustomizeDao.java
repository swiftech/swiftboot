package org.swiftboot.web.model.dao;

import org.swiftboot.web.model.entity.BaseIdEntity;

/**
 * @author swiftech
 */
public interface GenericCustomizeDao<T extends BaseIdEntity> {

    void saveEntity(T t);
}
