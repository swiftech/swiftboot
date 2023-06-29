package org.swiftboot.demo.result;

import org.swiftboot.web.result.Result;

/**
 * @author allen
 */
public class UserInfoResult implements Result {

    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
