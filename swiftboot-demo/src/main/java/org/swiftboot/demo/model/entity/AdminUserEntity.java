package org.swiftboot.demo.model.entity;

import org.springframework.context.annotation.Description;
import org.swiftboot.web.annotation.PropertyDescription;
import org.swiftboot.web.model.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 管理员
 *
 * @author swiftech 2020-01-06
 **/
@Description("管理员")
@Entity
@Table(name = "DEMO_ADMIN_USER")
public class AdminUserEntity extends BaseEntity {

    /**
     * Login name of administrator
     */
    @PropertyDescription(value = "Login name of administrator", example = "admin")
    @Column(name = "LOGIN_NAME", length = 32, unique = true, nullable = false, columnDefinition = "VARCHAR(32) UNIQUE NOT NULL COMMENT 'Login name of administrator'")
    private String loginName;

    /**
     * Login password to login name (MD5 with salt)
     */
    @PropertyDescription(value = "Login password to login name", notes = "MD5 with salt", example = "a865a7e0ddbf35fa6f6a232e0893bea4")
    @Column(name = "LOGIN_PWD", length = 64, nullable = false, columnDefinition = "VARCHAR(64) NOT NULL COMMENT 'Login password to login name(MD5 with salt)'")
    private String loginPwd;


    public AdminUserEntity() {
    }

    public AdminUserEntity(String id) {
        super(id);
    }

    /**
     * 获取Login name of administrator
     */
    public String getLoginName() {
        return loginName;
    }

    /**
     * 设置Login name of administrator
     */
    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    /**
     * 获取Login password to login name (MD5 with salt)
     */
    public String getLoginPwd() {
        return loginPwd;
    }

    /**
     * 设置Login password to login name (MD5 with salt)
     */
    public void setLoginPwd(String loginPwd) {
        this.loginPwd = loginPwd;
    }

}
