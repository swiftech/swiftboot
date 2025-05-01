package org.swiftboot.data.repository;

import org.springframework.data.repository.CrudRepository;
import org.swiftboot.data.model.entity.BaseIdEntity;

/**
 * @author swiftech
 */
public interface GenericRepository<T extends BaseIdEntity> extends BaseRepository, CrudRepository<T, String>, GenericCustomizeRepository<T> {

}
