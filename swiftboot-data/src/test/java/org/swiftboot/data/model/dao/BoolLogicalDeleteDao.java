package org.swiftboot.data.model.dao;

import org.springframework.data.repository.CrudRepository;
import org.swiftboot.data.model.entity.BoolLogicalDeleteEntity;

import java.util.List;

/**
 * Dao for testing logical deletion.
 *
 * @author swiftech
 */
public interface BoolLogicalDeleteDao extends CrudRepository<BoolLogicalDeleteEntity, String>,
        BooleanLogicalDeleteExtend<BoolLogicalDeleteEntity> {

    List<BoolLogicalDeleteEntity> findByIdIn(List<String> ids);
}
