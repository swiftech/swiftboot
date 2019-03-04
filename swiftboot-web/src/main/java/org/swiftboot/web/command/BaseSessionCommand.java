package org.swiftboot.web.command;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.swiftboot.web.model.entity.Persistent;

/**
 * @author swiftech
 * @param <E>
 */
public abstract class BaseSessionCommand<E extends Persistent> extends BasePopulateCommand<E>{

    @JsonIgnore
    public void setSessionId(String sessionId) {
        setHeader("session_id", sessionId);
    }

    @JsonIgnore
    public String getSessionId() {
        if (getHeaders() == null) {
            return null;
        }
        return getHeaders().get("session_id");
    }
}
