package org.swiftboot.web.model.dao.impl;

import org.springframework.transaction.annotation.Transactional;
import org.swiftboot.web.model.dao.GenericCustomizeDao;
import org.swiftboot.web.model.entity.BaseIdEntity;

/**
 * allen
 */
public class GenericCustomizeDaoImpl<T extends BaseIdEntity> extends BaseCustomizeDaoImpl<T> implements GenericCustomizeDao<T> {

    @Transactional
    public void saveEntity(T t){
        super.entityManager.persist(t);
    }
}
