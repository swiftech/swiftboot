package org.swiftboot.web.config;

/**
 * @author swiftech
 **/
public class FilterConfigBean {

    /**
     * 跨域访问允许的来源
     */
    private String corsAllowOrigin = "*";

    public String getCorsAllowOrigin() {
        return corsAllowOrigin;
    }

    public void setCorsAllowOrigin(String corsAllowOrigin) {
        this.corsAllowOrigin = corsAllowOrigin;
    }
}
