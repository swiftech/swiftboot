package org.swiftboot.demo.config;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 初始化数据使用, 暂时作为测试使用
 *
 * @author swiftech
 **/
public class InitConfigBean {

    /**
     * 用户
     */
    private Set<UserConfigBean> users;

    /**
     * 角色
     */
    private Set<RoleConfigBean> roles;

    /**
     * 权限
     */
    private Set<PermissionConfigBean> permissions;

    /**
     * 角色权限关系（角色 -> 权限code列表)
     */
    private Map<String, List<String>> rolePermRels;

    /**
     * 角色用户关系（角色 -> 用户登录名列表)
     */
    private Map<String, List<String>> roleUserRels;

    public Set<RoleConfigBean> getRoles() {
        return roles;
    }

    public void setRoles(Set<RoleConfigBean> roles) {
        this.roles = roles;
    }

    public Set<PermissionConfigBean> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<PermissionConfigBean> permissions) {
        this.permissions = permissions;
    }

    public Map<String, List<String>> getRolePermRels() {
        return rolePermRels;
    }

    public void setRolePermRels(Map<String, List<String>> rolePermRels) {
        this.rolePermRels = rolePermRels;
    }

    public Set<UserConfigBean> getUsers() {
        return users;
    }

    public void setUsers(Set<UserConfigBean> users) {
        this.users = users;
    }

    public Map<String, List<String>> getRoleUserRels() {
        return roleUserRels;
    }

    public void setRoleUserRels(Map<String, List<String>> roleUserRels) {
        this.roleUserRels = roleUserRels;
    }

}
