package org.swiftboot.demo.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.context.annotation.Description;
import org.swiftboot.web.annotation.PropertyDescription;
import org.swiftboot.web.model.entity.BaseEntity;

import javax.persistence.*;
import java.util.Set;

/**
 * 管理员用户角色
 *
 * @author swiftech
 **/
@Description("管理员用户角色")
@Entity
@Table(name = "DEMO_ADMIN_ROLE")
public class AdminRoleEntity extends BaseEntity {

    /**
     * 管理员用户角色名称
     */
    @PropertyDescription(value = "管理员用户角色名称", example = "经理")
    @Column(name = "ROLE_NAME", length = 32, nullable = false, columnDefinition = "CHAR(32) NOT NULL COMMENT '管理员用户角色名称'")
    private String roleName;

    /**
     * 管理员用户角色描述
     */
    @PropertyDescription(value = "管理员用户角色描述", example = "至高无上")
    @Column(name = "ROLE_DESC", length = 256, nullable = false, columnDefinition = "VARCHAR(256) NOT NULL COMMENT '管理员用户角色描述'")
    private String roleDesc;

    /**
     * 用户角色关联
     */
    @JsonIgnore
    @PropertyDescription(value = "用户角色关联")
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "adminRole")
    private Set<AdminUserRoleRelEntity> adminUserRoleRels;

    /**
     * 角色权限关联
     */
    @PropertyDescription(value = "角色权限关联")
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "adminRole")
    private Set<AdminRolePermissionRelEntity> adminRolePermissionRels;


    public AdminRoleEntity() {
    }

    public AdminRoleEntity(String id) {
        super(id);
    }

    /**
     * 获取管理员用户角色名称
     */
    public String getRoleName() {
        return roleName;
    }

    /**
     * 设置管理员用户角色名称
     */
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    /**
     * 获取管理员用户角色描述
     */
    public String getRoleDesc() {
        return roleDesc;
    }

    /**
     * 设置管理员用户角色描述
     */
    public void setRoleDesc(String roleDesc) {
        this.roleDesc = roleDesc;
    }


    /**
     * 获取用户角色关联
     */
    public Set<AdminUserRoleRelEntity> getAdminUserRoleRels() {
        return adminUserRoleRels;
    }

    /**
     * 设置用户角色关联
     */
    public void setAdminUserRoleRels(Set<AdminUserRoleRelEntity> adminUserRoleRels) {
        this.adminUserRoleRels = adminUserRoleRels;
    }

    /**
     * 获取角色权限关联
     */
    public Set<AdminRolePermissionRelEntity> getAdminRolePermissionRels() {
        return adminRolePermissionRels;
    }

    /**
     * 设置角色权限关联
     */
    public void setAdminRolePermissionRels(Set<AdminRolePermissionRelEntity> adminRolePermissionRels) {
        this.adminRolePermissionRels = adminRolePermissionRels;
    }


    @Override
    public String toString() {
        return "AdminRoleEntity{" +
                "roleName='" + roleName + '\'' +
                ", roleDesc='" + roleDesc + '\'' +
                ", adminUserRoleRels=" + adminUserRoleRels +
                ", adminRolePermissionRels=" + adminRolePermissionRels +
                '}';
    }
}
