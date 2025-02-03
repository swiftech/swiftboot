package org.swiftboot.demo.model.entity;

import org.springframework.context.annotation.Description;
import org.swiftboot.data.annotation.PropertyDescription;
import org.swiftboot.data.model.entity.BaseEntity;

import jakarta.persistence.*;

/**
 * 用户角色关联
 *
 * @author swiftech
 **/
@Description("用户角色关联")
@Entity
@Table(name = "DEMO_ADMIN_USER_ROLE_REL")
public class AdminUserRoleRelEntity extends BaseEntity {

    /**
     * 管理员用户
     */
    @PropertyDescription(value = "管理员用户", example = "a2ff207bff038d313888f172d7bc1abf")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ADMIN_USER_ID", nullable = false)
    private AdminUserEntity adminUser;

    /**
     * 管理员角色
     */
    @PropertyDescription(value = "管理员角色", example = "b2a1dbe6fc6ac11cff86aba687c50cf3")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ADMIN_ROLE_ID", nullable = false)
    private AdminRoleEntity adminRole;


    public AdminUserRoleRelEntity() {
    }

    public AdminUserRoleRelEntity(String id) {
        super(id);
    }

    /**
     * 获取管理员用户
     */
    public AdminUserEntity getAdminUser() {
        return adminUser;
    }

    /**
     * 设置管理员用户
     */
    public void setAdminUser(AdminUserEntity adminUser) {
        this.adminUser = adminUser;
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

}
