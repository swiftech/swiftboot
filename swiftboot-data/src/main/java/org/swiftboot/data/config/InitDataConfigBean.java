package org.swiftboot.data.config;

/**
 * @author swiftech
 * @since 2.1
 */
public class InitDataConfigBean {

    /**
     * 是否初始化数据库
     */
    private boolean enabled = false;

    /**
     * 初始化数据库的数据文件路径（classpath下）
     */
    private String baseDir = "init/";

    /**
     * 加载数据文件时如果没找到，是否需要 fallback 到默认的目录（default/) 下加载文件，默认为 false
     */
    private boolean fallback = false;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isFallback() {
        return fallback;
    }

    public void setFallback(boolean fallback) {
        this.fallback = fallback;
    }

    public String getBaseDir() {
        return baseDir;
    }

    public void setBaseDir(String baseDir) {
        this.baseDir = baseDir;
    }

}
