package org.swiftboot.auth.controller;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.swiftboot.data.model.entity.IdPersistable;
import org.swiftboot.web.command.BasePopulateCommand;

/**
 * If user authenticated, user id can be automatically set.
 *
 * @author swiftech
 * @since 2.1
 * @see org.swiftboot.auth.aop.UserSessionAdvice
 */
public class BaseAuthenticatedCommand<E extends IdPersistable> extends BasePopulateCommand<E> {

    @JsonIgnore
    private String userId;

    @JsonIgnore
    private String userName;

    @JsonIgnore
    public String getUserId() {
        return userId;
    }

    @JsonIgnore
    public void setUserId(String userId) {
        this.userId = userId;
    }

    @JsonIgnore
    public String getUserName() {
        return userName;
    }

    @JsonIgnore
    public void setUserName(String userName) {
        this.userName = userName;
    }
}
