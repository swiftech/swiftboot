package org.swiftboot.auth.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.swiftboot.data.model.entity.IdPersistable;
import org.swiftboot.web.request.BasePopulateRequest;

/**
 * Extends your request classes from BaseAuthenticatedRequest to have user id and username injected automatically.
 * If user is authenticated successfully, user id and username can be automatically set.
 *
 * @author swiftech
 * @since 2.1
 * @see org.swiftboot.auth.aop.UserSessionAdvice
 */
public class BaseAuthenticatedRequest<E extends IdPersistable> extends BasePopulateRequest<E> {

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
