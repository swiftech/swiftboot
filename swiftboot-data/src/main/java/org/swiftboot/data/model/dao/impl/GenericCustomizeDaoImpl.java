package org.swiftboot.data.model.dao.impl;

import org.springframework.transaction.annotation.Transactional;
import org.swiftboot.data.model.dao.GenericCustomizeDao;
import org.swiftboot.data.model.entity.BaseIdEntity;

/**
 * @author swiftech
 */
public class GenericCustomizeDaoImpl<T extends BaseIdEntity> extends BaseCustomizeDaoImpl<T> implements GenericCustomizeDao<T> {

    @Transactional
    public void saveEntity(T t){
        super.entityManager.persist(t);
    }
}
