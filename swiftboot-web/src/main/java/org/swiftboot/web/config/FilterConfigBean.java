package org.swiftboot.web.config;

import java.util.List;

/**
 * @author swiftech
 **/
public class FilterConfigBean {

    /**
     * 跨域访问允许的来源
     */
    private String corsAllowOrigin = "*";

    /**
     * 允许的头
     */
    private List<String> allowHeaders;

    public String getCorsAllowOrigin() {
        return corsAllowOrigin;
    }

    public void setCorsAllowOrigin(String corsAllowOrigin) {
        this.corsAllowOrigin = corsAllowOrigin;
    }

    public List<String> getAllowHeaders() {
        return allowHeaders;
    }

    public void setAllowHeaders(List<String> allowHeaders) {
        this.allowHeaders = allowHeaders;
    }
}
