package org.swiftboot.data.repository;

import org.springframework.data.repository.CrudRepository;
import org.swiftboot.data.model.entity.ChildEntity;

import java.util.Optional;

/**
 * @author swiftech
 */
public interface ChildRepository extends CrudRepository<ChildEntity, String> {

    Optional<ChildEntity> findByName(String name);
}
