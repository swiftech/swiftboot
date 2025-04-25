package org.swiftboot.data.model.dao;

import org.springframework.data.repository.CrudRepository;
import org.swiftboot.data.model.entity.IntLogicalDeleteEntity;
import org.swiftboot.data.repository.IntegerLogicalDeleteExtend;

/**
 * Dao for testing logical deletion.
 *
 * @author swiftech
 */
public interface IntLogicalDeleteDao extends CrudRepository<IntLogicalDeleteEntity, String>,
        IntegerLogicalDeleteExtend<IntLogicalDeleteEntity> {

}
