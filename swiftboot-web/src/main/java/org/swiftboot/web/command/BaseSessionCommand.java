package org.swiftboot.web.command;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import org.swiftboot.data.model.entity.Persistent;
import org.swiftboot.web.constant.HttpConstants;

/**
 * @param <E>
 * @author swiftech
 */
@ApiModel
public abstract class BaseSessionCommand<E extends Persistent> extends BasePopulateCommand<E> {

    @JsonIgnore
    public void setSessionId(String sessionId) {
        setHeader(HttpConstants.DEFAULT_SESSION_ID_NAME, sessionId);
    }

    @JsonIgnore
    public String getSessionId() {
        if (getHeaders() == null) {
            return null;
        }
        return getHeaders().get(HttpConstants.DEFAULT_SESSION_ID_NAME);
    }
}
