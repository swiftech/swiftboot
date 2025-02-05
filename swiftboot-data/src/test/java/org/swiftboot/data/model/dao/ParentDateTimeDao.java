package org.swiftboot.data.model.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.swiftboot.data.model.entity.ParentDateTimeEntity;

/**
 * @author swiftech
 **/
@Repository
public interface ParentDateTimeDao extends PagingAndSortingRepository<ParentDateTimeEntity, String>, CrudRepository<ParentDateTimeEntity, String> {

}
