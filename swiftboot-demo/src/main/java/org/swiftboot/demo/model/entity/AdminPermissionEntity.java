package org.swiftboot.demo.model.entity;

import org.springframework.context.annotation.Description;
import org.swiftboot.data.annotation.PropertyDescription;
import org.swiftboot.data.model.entity.BaseEntity;

import javax.persistence.*;
import java.util.Set;

/**
 * 管理员用户权限
 *
 * @author swiftech
 **/
@Description("管理员用户权限")
@Entity
@Table(name = "DEMO_ADMIN_PERMISSION")
public class AdminPermissionEntity extends BaseEntity {

    /**
     * 管理员用户权限代码
     */
    @PropertyDescription(value = "管理员用户权限代码", example = "ORDER_DEL")
    @Column(name = "PERM_CODE", length = 256, unique = true, nullable = false, columnDefinition = "VARCHAR(256) UNIQUE NOT NULL COMMENT '管理员用户权限代码'")
    private String permCode;

    /**
     * 管理员用户权限描述
     */
    @PropertyDescription(value = "管理员用户权限描述")
    @Column(name = "PERM_DESC", length = 256, nullable = false, columnDefinition = "VARCHAR(256) NOT NULL COMMENT '管理员用户权限描述'")
    private String permDesc;

    /**
     * 是否废弃
     */
    @PropertyDescription(value = "是否废弃", example = "false")
    @Column(name = "IS_OBSOLETE", nullable = false, columnDefinition = "BOOL NOT NULL DEFAULT false COMMENT '是否废弃'")
    private Boolean isObsolete = Boolean.FALSE;

    /**
     * 父权限
     */
    @PropertyDescription(value = "父权限")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARENT_ID", nullable = true)
    private AdminPermissionEntity parentPermission;

    /**
     * 子权限集合
     */
    @PropertyDescription(value = "子权限集合")
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "parentPermission")
    private Set<AdminPermissionEntity> subPermissions;

    /**
     * 角色权限关联
     */
    @PropertyDescription(value = "角色权限关联")
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "adminPermission")
    private Set<AdminRolePermissionRelEntity> adminRolePermissionRels;


    public AdminPermissionEntity() {
    }

    public AdminPermissionEntity(String id) {
        super(id);
    }

    /**
     * 获取管理员用户权限代码
     */
    public String getPermCode() {
        return permCode;
    }

    /**
     * 设置管理员用户权限代码
     */
    public void setPermCode(String permCode) {
        this.permCode = permCode;
    }

    /**
     * 获取管理员用户权限描述
     */
    public String getPermDesc() {
        return permDesc;
    }

    /**
     * 设置管理员用户权限描述
     */
    public void setPermDesc(String permDesc) {
        this.permDesc = permDesc;
    }

    /**
     * 获取是否废弃
     */
    public Boolean getIsObsolete() {
        return isObsolete;
    }

    /**
     * 设置是否废弃
     */
    public void setIsObsolete(Boolean isObsolete) {
        this.isObsolete = isObsolete;
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

    public Set<AdminPermissionEntity> getSubPermissions() {
        return subPermissions;
    }

    public void setSubPermissions(Set<AdminPermissionEntity> subPermissions) {
        this.subPermissions = subPermissions;
    }

    public AdminPermissionEntity getParentPermission() {
        return parentPermission;
    }

    public void setParentPermission(AdminPermissionEntity parentPermission) {
        this.parentPermission = parentPermission;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AdminPermissionEntity that = (AdminPermissionEntity) o;

        return permCode.equals(that.permCode);

    }

    @Override
    public int hashCode() {
        return permCode.hashCode();
    }
}
