package org.swiftboot.data.repository;

import org.springframework.data.repository.CrudRepository;
import org.swiftboot.data.model.entity.BaseIdEntity;

/**
 * @author swiftech 2019-08-28
 **/
public interface BaseRepository<T extends BaseIdEntity> extends CrudRepository<T, String> {
}
