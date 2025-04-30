package org.swiftboot.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.springframework.context.annotation.Description;
import org.swiftboot.auth.model.UserPersistable;
import org.swiftboot.data.annotation.PropertyDescription;
import org.swiftboot.data.model.entity.BaseLocalDateTimeEntity;

/**
 * App用户
 *
 * @author swiftech 2020-02-05
 **/
@Description("App用户")
@Entity
@Table(name = "DEMO_APP_USER")
public class AppUserEntity extends BaseLocalDateTimeEntity implements UserPersistable {

    /**
     * Login name of app user
     */
    @PropertyDescription(value = "Login name of app user", example = "13866669999")
    @Column(name = "LOGIN_NAME", length = 32, unique = true, nullable = false)
    private String loginName;

    /**
     * Login password to login name (MD5 with salt)
     */
    @PropertyDescription(value = "Login password to login name", notes = "MD5 with salt", example = "a43b66902590c003c213a5ed1b6f92e3")
    @Column(name = "LOGIN_PWD", length = 64, nullable = false)
    private String loginPwd;


    public AppUserEntity() {
    }

    public AppUserEntity(String id) {
        super(id);
    }

    /**
     * 获取Login name of app user
     */
    @Override
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
    @Override
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
