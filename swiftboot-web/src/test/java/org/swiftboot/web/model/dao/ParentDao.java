package org.swiftboot.web.model.dao;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.swiftboot.web.model.entity.ParentEntity;

/**
 * @author swiftech
 **/
@Repository
public interface ParentDao extends PagingAndSortingRepository<ParentEntity, String> {

}
