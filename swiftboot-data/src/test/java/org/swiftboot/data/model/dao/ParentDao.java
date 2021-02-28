package org.swiftboot.data.model.dao;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.swiftboot.data.model.entity.ParentEntity;

import java.util.Optional;

/**
 * @author swiftech
 **/
@Repository
public interface ParentDao extends PagingAndSortingRepository<ParentEntity, String> {

    Optional<ParentEntity> findByName(String name);
}
