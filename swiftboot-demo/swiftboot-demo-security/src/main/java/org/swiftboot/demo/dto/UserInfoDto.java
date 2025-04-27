package org.swiftboot.demo.dto;

import org.swiftboot.demo.model.UserEntity;
import org.swiftboot.web.annotation.PopulateIgnore;
import org.swiftboot.web.dto.BasePopulateDto;
import org.swiftboot.web.dto.Dto;

/**
 * @author swiftech
 */
public class UserInfoDto extends BasePopulateDto<UserEntity> implements Dto {

    private String id;

    private String loginName;

    private String nickName;

    /**
     * permission codes separated with comma.
     */
    @PopulateIgnore
    private String permissions;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPermissions() {
        return permissions;
    }

    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }
}
