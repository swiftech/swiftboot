package org.swiftboot.demo.result;

import org.swiftboot.web.result.Result;

/**
 * @author swiftech
 */
public class UserInfoResult implements Result {

    private String userName;

    /**
     * permission codes separated with comma.
     */
    private String permissions;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPermissions() {
        return permissions;
    }

    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }
}
