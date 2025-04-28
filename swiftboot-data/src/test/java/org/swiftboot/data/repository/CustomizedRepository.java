package org.swiftboot.data.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.swiftboot.data.model.entity.CustomizedEntity;

import java.util.Optional;

/**
 * @author swiftech
 **/
@Repository
public interface CustomizedRepository extends PagingAndSortingRepository<CustomizedEntity, String>, CrudRepository<CustomizedEntity, String> {

    Optional<CustomizedEntity> findByName(String name);
}
