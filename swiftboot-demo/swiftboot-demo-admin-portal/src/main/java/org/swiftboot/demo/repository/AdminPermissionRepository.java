package org.swiftboot.demo.repository;

import org.springframework.data.repository.CrudRepository;
import org.swiftboot.demo.model.AdminPermissionEntity;

import java.util.List;
import java.util.Optional;

/**
 * @author swiftech
 */
public interface AdminPermissionRepository extends CrudRepository<AdminPermissionEntity, String> {

    /**
     *
     */
    Optional<AdminPermissionEntity> findByPermCode(String permCode);

    /**
     * 按照父权限的 code 查询所有子权限
     *
     * @param parentPermCode
     * @return
     */
    List<AdminPermissionEntity> findAdminPermissionEntitiesByPermCodeIsStartingWith(String parentPermCode);
}
