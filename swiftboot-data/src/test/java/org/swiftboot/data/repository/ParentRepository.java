package org.swiftboot.data.repository;

import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.swiftboot.data.model.entity.ParentEntity;

import jakarta.persistence.QueryHint;
import java.util.Optional;

/**
 * @author swiftech
 **/
@Repository
public interface ParentRepository extends PagingAndSortingRepository<ParentEntity, String>, CrudRepository<ParentEntity, String> {

    @QueryHints(value = { @QueryHint(name = org.hibernate.annotations.QueryHints.FLUSH_MODE, value = "COMMIT") })
    Optional<ParentEntity> findByName(String name);
}
