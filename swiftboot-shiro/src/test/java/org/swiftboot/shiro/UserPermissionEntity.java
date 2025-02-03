package org.swiftboot.shiro;

import org.swiftboot.data.model.entity.BaseIdEntity;
import org.swiftboot.shiro.model.entity.PermissionEntityStub;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * @author swiftech
 */
@Entity
@Table(name = "UT_USER_PERMISSION")
public class UserPermissionEntity extends BaseIdEntity implements PermissionEntityStub {

    /**
     * 登录用户名
     */
    @Column(name = "LOGIN_NAME", length = 32, unique = true, nullable = false)
    private String loginName;

    @Column
    private String permCode = "all";

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    @Override
    public String getPermissionCode() {
        return permCode;
    }

    public void setPermCode(String permCode) {
        this.permCode = permCode;
    }
}
