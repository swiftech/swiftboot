package org.swiftboot.demo.repository;

import org.springframework.data.repository.CrudRepository;
import org.swiftboot.demo.model.AdminUserPermissionView;

import java.util.List;

/**
 * @author swiftech
 * @deprecated
 */
public interface AdminUserPermissionRepository
        extends CrudRepository<AdminUserPermissionView, String>{//, UserPermissionDaoStub<AdminUserPermissionView> {

    List<AdminUserPermissionView> findPermissionsByLoginName(String loginName);
}
