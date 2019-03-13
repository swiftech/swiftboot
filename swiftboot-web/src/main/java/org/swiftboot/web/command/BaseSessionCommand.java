package org.swiftboot.web.command;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import org.swiftboot.web.constant.HttpConstants;
import org.swiftboot.web.model.entity.Persistent;

/**
 * @param <E>
 * @author swiftech
 */
@ApiModel
public abstract class BaseSessionCommand<E extends Persistent> extends BasePopulateCommand<E> {

    @JsonIgnore
    public void setSessionId(String sessionId) {
        setHeader(HttpConstants.SESSION_ID_NAME, sessionId);
    }

    @JsonIgnore
    public String getSessionId() {
        if (getHeaders() == null) {
            return null;
        }
        return getHeaders().get(HttpConstants.SESSION_ID_NAME);
    }
}
