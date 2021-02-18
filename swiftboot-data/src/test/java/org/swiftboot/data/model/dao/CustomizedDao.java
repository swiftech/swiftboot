package org.swiftboot.data.model.dao;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.swiftboot.data.model.entity.CustomizedEntity;

/**
 * @author swiftech
 **/
@Repository
public interface CustomizedDao extends PagingAndSortingRepository<CustomizedEntity, String> {

}
