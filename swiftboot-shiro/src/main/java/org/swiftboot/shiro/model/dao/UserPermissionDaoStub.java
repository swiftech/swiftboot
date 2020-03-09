package org.swiftboot.shiro.model.dao;

import org.swiftboot.shiro.model.entity.PermissionEntityStub;

import java.util.List;

/**
 * 权限 Dao 的抽象，提供了同一个应用实现不同的用户权限控制。
 * 实现类可以考虑采用数据库视图或者缓存来实现权限
 *
 * @param <T> 用户权限实体类型
 * @since 1.2
 */
public interface UserPermissionDaoStub<T extends PermissionEntityStub> {

    /**
     * 通过用户查找他的权限列表
     *
     * @param loginName
     * @return
     */
    List<T> findPermissionsByLoginName(String loginName);
}
