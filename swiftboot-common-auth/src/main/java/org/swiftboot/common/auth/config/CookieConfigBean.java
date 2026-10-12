package org.swiftboot.common.auth.config;

/**
 * @since 3.2.1
 */
public class CookieConfigBean {

    /**
     * 域名，支持子域名匹配，例如 example.com 匹配 a.example.com, b.example.com
     */
    private String domain;

    /**
     * 设置 Cookie 中用户 Token 的有效路径，默认为 "/"。
     */
    private String path = "/";

    /**
     * Cookie 失效时间，单位秒
     */
    private Integer maxAge = 24 * 60 * 60;

    /**
     * 生产环境必须开启HTTPS，仅HTTPS传输
     */
    private Boolean secure = true;

    /**
     * 防范XSS攻击：禁止JS读取Cookie
     */
    private Boolean httpOnly = true;

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(Integer maxAge) {
        this.maxAge = maxAge;
    }

    public Boolean getSecure() {
        return secure;
    }

    public void setSecure(Boolean secure) {
        this.secure = secure;
    }

    public Boolean getHttpOnly() {
        return httpOnly;
    }

    public void setHttpOnly(Boolean httpOnly) {
        this.httpOnly = httpOnly;
    }

    @Override
    public String toString() {
        return "CookieConfigBean{" +
                "path='" + path + '\'' +
                ", maxAge=" + maxAge +
                ", secure=" + secure +
                ", httpOnly=" + httpOnly +
                '}';
    }
}
