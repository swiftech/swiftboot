package org.swiftboot.data.repository.impl;

import org.springframework.transaction.annotation.Transactional;
import org.swiftboot.data.repository.GenericCustomizeRepository;
import org.swiftboot.data.model.entity.BaseIdEntity;

/**
 * @author swiftech
 */
public class GenericCustomizeRepositoryImpl<T extends BaseIdEntity> extends BaseCustomizeRepositoryImpl<T> implements GenericCustomizeRepository<T> {

    @Transactional
    public void saveEntity(T t){
        super.entityManager.persist(t);
    }
}
