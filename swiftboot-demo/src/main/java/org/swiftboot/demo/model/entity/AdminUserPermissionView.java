package org.swiftboot.demo.model.entity;

import org.springframework.data.annotation.Immutable;
import org.swiftboot.shiro.model.entity.PermissionEntityStub;
import org.swiftboot.web.model.entity.BaseIdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 *
 */
@Entity
@Table(name = "V_ADMIN_USER_PERMISSIONS")
@Immutable
public class AdminUserPermissionView extends BaseIdEntity implements PermissionEntityStub {

    /**
     * 登录用户名
     */
    public static final String COL_NAME_LOGIN_NAME = "LOGIN_NAME";

    /**
     * 角色名称
     */
    public static final String COL_NAME_ROLE_NAME = "ROLE_NAME";

    /**
     * 描述信息
     */
    public static final String COL_NAME_ROLE_DESC = "ROLE_DESC";

    /**
     * 权限代码
     */
    public static final String COL_NAME_PERMISSION_CODE = "PERM_CODE";

    /**
     * 描述信息
     */
    public static final String COL_NAME_PERM_DESC = "PERM_DESC";


    /**
     * 登录用户名
     */
    @Column(name = COL_NAME_LOGIN_NAME, length = 32, unique = true, nullable = false)
    private String loginName;

    /**
     * 角色名称
     */
    @Column(name = COL_NAME_ROLE_NAME, length = 32, unique = true, nullable = false)
    private String roleName;

    /**
     * 描述信息
     */
    @Column(name = COL_NAME_ROLE_DESC, length = 256)
    private String roleDesc;


    /**
     * 权限代码
     */
    @Column(name = COL_NAME_PERMISSION_CODE, length = 32)
    private String permCode;

    /**
     * 描述信息
     */
    @Column(name = COL_NAME_PERM_DESC, length = 256)
    private String permDesc;


    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleDesc() {
        return roleDesc;
    }

    public void setRoleDesc(String roleDesc) {
        this.roleDesc = roleDesc;
    }

    @Override
    public String getPermissionCode() {
        return permCode;
    }

    public void setPermCode(String permCode) {
        this.permCode = permCode;
    }

    public String getPermDesc() {
        return permDesc;
    }

    public void setPermDesc(String permDesc) {
        this.permDesc = permDesc;
    }
}
