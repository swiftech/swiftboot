package org.swiftboot.shiro.config;

/**
 * @author swiftech
 **/
public class CookieConfigBean {

    private String domain = "localhost";

    private String path = "/";

    private String name = "swiftboot_shiro_token";

    /**
     * Cookie 过期时间，默认半小时
     */
    private int maxAge = 1800;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }
}
