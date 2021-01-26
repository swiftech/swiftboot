package org.swiftboot.data.model.dao;

import org.springframework.data.repository.CrudRepository;
import org.swiftboot.data.model.entity.BaseIdEntity;

/**
 * @author swiftech
 */
public interface GenericDao<T extends BaseIdEntity> extends BaseDao, CrudRepository<T, String>, GenericCustomizeDao<T> {

}
