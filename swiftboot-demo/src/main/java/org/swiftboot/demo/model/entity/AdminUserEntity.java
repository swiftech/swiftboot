package org.swiftboot.demo.model.entity;

import org.springframework.context.annotation.Description;
import org.swiftboot.data.annotation.PropertyDescription;
import org.swiftboot.data.model.entity.BaseEntity;
import org.swiftboot.shiro.model.entity.UserEntityStub;

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
public class AdminUserEntity extends BaseEntity implements UserEntityStub {

    /**
     * 登录名
     */
    @PropertyDescription(value = "Login name of administrator", example = "admin")
    @Column(name = "LOGIN_NAME", length = 32, unique = true, nullable = false, columnDefinition = "VARCHAR(32) NOT NULL COMMENT 'Login name of administrator'")
    private String loginName;

    /**
     * 登录密码 (MD5加盐)
     */
    @PropertyDescription(value = "Login password to login name", notes = "MD5 with salt", example = "a865a7e0ddbf35fa6f6a232e0893bea4")
    @Column(name = "LOGIN_PWD", length = 64, nullable = false, columnDefinition = "VARCHAR(64) NOT NULL COMMENT 'Login password to login name(MD5 with salt)'")
    private String loginPwd;

    /**
     * 用户姓名
     */
    @PropertyDescription(value = "Name of the user", example = "James Bond")
    @Column(name = "USER_NAME", length = 64, columnDefinition = "VARCHAR(64) COMMENT 'Name of the user'")
    private String userName;


    public AdminUserEntity() {
    }

    public AdminUserEntity(String id) {
        super(id);
    }

    /**
     * 获取登录名
     */
    public String getLoginName() {
        return loginName;
    }

    /**
     * 设置登录名
     */
    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    /**
     * 获取登录密码 (MD5加盐)
     */
    public String getLoginPwd() {
        return loginPwd;
    }

    /**
     * 设置登录密码 (MD5加盐)
     */
    public void setLoginPwd(String loginPwd) {
        this.loginPwd = loginPwd;
    }

    /**
     * 获取用户姓名
     */
    public String getUserName() {
        return userName;
    }

    /**
     * 设置用户姓名
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

}
