package org.swiftboot.common.auth.request;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * All request class should inherit me to have the ability to extinguish clients for user.
 *
 * @since 3.2
 */
public class BaseAuthRequest {

    /**
     *
     */
    @Schema(description = "客户端来源", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    protected String clientSource;

    public BaseAuthRequest() {
    }

    public BaseAuthRequest(String clientSource) {
        this.clientSource = clientSource;
    }

    public String getClientSource() {
        return clientSource;
    }

    public void setClientSource(String clientSource) {
        this.clientSource = clientSource;
    }
}
