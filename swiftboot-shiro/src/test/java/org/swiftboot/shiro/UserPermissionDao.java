package org.swiftboot.shiro;

import org.springframework.data.repository.CrudRepository;
import org.swiftboot.shiro.model.dao.UserPermissionDaoStub;

/**
 * @author swiftech
 */
public interface UserPermissionDao
        extends CrudRepository<UserPermissionEntity, String>, UserPermissionDaoStub<UserPermissionEntity> {


}
