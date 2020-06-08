package org.swiftboot.web.config;

import org.springframework.web.filter.CorsFilter;

import java.util.LinkedList;
import java.util.List;

/**
 * @author swiftech
 **/
public class FilterConfigBean {

    /**
     * 是否允许跨域访问，默认为 false。
     * 设置为 true 会对跨域访问不做任何限制，所以仅用于开发调试，生产环境如果需要跨域访问则必须自行配置 Spring 的跨域过滤器 {@link CorsFilter}
     */
    boolean cors = false;

    /**
     * 跨域访问允许的来源
     * @deprecated
     */
    private String corsAllowOrigin = "*";

    /**
     * 允许的头
     * @deprecated
     */
    private List<String> allowHeaders = new LinkedList<String>() {
        {
            add("*");
        }
    };

    public boolean isCors() {
        return cors;
    }

    public void setCors(boolean cors) {
        this.cors = cors;
    }

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
