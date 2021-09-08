package org.swiftboot.shiro;

import org.swiftboot.data.model.entity.BaseIdEntity;
import org.swiftboot.shiro.model.entity.UserEntityStub;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author swiftech
 */
@Entity
@Table(name = "UT_USER")
public class UserEntity extends BaseIdEntity implements UserEntityStub {

    @Column
    private String loginName;

    @Column
    private String loginPwd;

    public UserEntity() {
    }

    public UserEntity(String loginName) {
        this.loginName = loginName;
    }

    public UserEntity(String id, String loginName) {
        super(id);
        this.loginName = loginName;
    }

    @Override
    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    @Override
    public String getLoginPwd() {
        return loginPwd;
    }

    public void setLoginPwd(String loginPwd) {
        this.loginPwd = loginPwd;
    }
}
