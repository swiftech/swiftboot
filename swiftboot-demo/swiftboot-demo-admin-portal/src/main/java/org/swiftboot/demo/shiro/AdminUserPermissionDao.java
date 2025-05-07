package org.swiftboot.demo.shiro;

import org.swiftboot.demo.model.entity.AdminUserPermissionView;
import org.springframework.data.repository.CrudRepository;
import org.swiftboot.shiro.model.dao.UserPermissionDaoStub;

/**
 * @author swiftech
 * @deprecated
 */
public interface AdminUserPermissionDao
        extends CrudRepository<AdminUserPermissionView, String>, UserPermissionDaoStub<AdminUserPermissionView> {
}
