package org.swiftboot.data.repository;

import org.springframework.data.repository.CrudRepository;
import org.swiftboot.data.model.entity.IntLogicalDeleteEntity;

/**
 * Dao for testing logical deletion.
 *
 * @author swiftech
 */
public interface IntLogicalDeleteRepository extends CrudRepository<IntLogicalDeleteEntity, String>,
        IntegerLogicalDeleteExtend<IntLogicalDeleteEntity> {

}
