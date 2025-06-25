package org.swiftboot.demo.model;

import org.springframework.context.annotation.Description;
import org.swiftboot.data.annotation.PropertyDescription;
import org.swiftboot.data.model.entity.BaseBoolDeleteEntity;

import jakarta.persistence.*;

/**
 * 角色权限关联
 *
 * @author swiftech
 **/
@Description("角色权限关联")
@Entity
@Table(name = "DEMO_ADMIN_ROLE_PERMISSION_REL")
public class AdminRolePermissionRelEntity extends BaseBoolDeleteEntity {

    /**
     * 管理员角色
     */
    @PropertyDescription(value = "管理员角色", example = "b2a1dbe6fc6ac11cff86aba687c50cf3")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ADMIN_ROLE_ID", nullable = false)
    private AdminRoleEntity adminRole;

    /**
     * 管理员权限
     */
    @PropertyDescription(value = "管理员权限", example = "96cf48ea8b088cd45328622c298ec5aa")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ADMIN_PERMISSION_ID", nullable = false)
    private AdminPermissionEntity adminPermission;


    public AdminRolePermissionRelEntity() {
    }

    public AdminRolePermissionRelEntity(String id) {
        super(id);
    }

    /**
     * 获取管理员角色
     */
    public AdminRoleEntity getAdminRole() {
        return adminRole;
    }

    /**
     * 设置管理员角色
     */
    public void setAdminRole(AdminRoleEntity adminRole) {
        this.adminRole = adminRole;
    }

    /**
     * 获取管理员权限
     */
    public AdminPermissionEntity getAdminPermission() {
        return adminPermission;
    }

    /**
     * 设置管理员权限
     */
    public void setAdminPermission(AdminPermissionEntity adminPermission) {
        this.adminPermission = adminPermission;
    }
}
