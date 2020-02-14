package org.swiftboot.demo.shiro;

import org.springframework.data.repository.CrudRepository;
import org.swiftboot.demo.model.entity.AdminUserPermissionView;
import org.swiftboot.shiro.model.dao.UserPermissionDaoStub;

/**
 * @author swiftech
 */
public interface AdminUserPermissionViewDao extends CrudRepository<AdminUserPermissionView, String>, UserPermissionDaoStub<AdminUserPermissionView> {
}
