package org.swiftboot.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.springframework.context.annotation.Description;
import org.swiftboot.data.annotation.PropertyDescription;
import org.swiftboot.data.model.entity.BaseLocalDateTimeEntity;

/**
 * App用户
 *
 * @author swiftech 2020-02-05
 **/
@Description("App用户")
@Entity
@Table(name = "DEMO_USER")
public class UserEntity extends BaseLocalDateTimeEntity {

    @Column(unique = true, length = 64)
    private String openId;

    /**
     * Login name of user
     */
    @PropertyDescription(value = "Login name of user", example = "13866669999")
    @Column(name = "LOGIN_NAME", length = 32, unique = true, nullable = false)
    private String loginName;

    /**
     * Login password to login name (MD5 with salt)
     */
    @PropertyDescription(value = "Login password to login name", notes = "MD5 with salt", example = "a43b66902590c003c213a5ed1b6f92e3")
    @Column(name = "LOGIN_PWD", length = 64, nullable = false)
    private String loginPwd;


    @PropertyDescription(value = "Login name of app user", example = "Darth Vader")
    @Column(name = "NICK_NAME", length = 64)
    private String nickName;

    @PropertyDescription(value = "Role of the user", example = "Emperor")
    @Column(name = "ROLE", length = 64)
    private String role;

    @PropertyDescription(value = "Permission code separated with comma", example = "perm_a, perm_b")
    @Column(name = "PERMISSIONS", length = 1024)
    private String permissions;


    public UserEntity() {
    }

    public UserEntity(String id) {
        super(id);
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    /**
     * 获取Login name of app user
     */
    public String getLoginName() {
        return loginName;
    }

    /**
     * 设置Login name of app user
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

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPermissions() {
        return permissions;
    }

    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }
}
