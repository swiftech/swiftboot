package org.swiftboot.data.config;

import org.swiftboot.data.constant.AutoUpdateTimeStrategy;

/**
 * @author swiftech
 **/
public class ModelConfigBean {

    /**
     * 是否自动生成主键，默认为 false
     */
    private boolean autoGenerateId = false;

    /**
     * 是否自动设置更新时间
     */
    private String autoUpdateTimeStrategy = AutoUpdateTimeStrategy.AUTO_UPDATE_TIME_ON_CHANGE;

    /**
     * 是否初始化数据库
     */
    private boolean initData = false;

    /**
     * 初始化数据库的数据文件路径
     */
    private String initBaseDir = "init/";

    /**
     * 初始化数据库扫描的包范围
     */
    private String[] initBaseEntityPackages;

    /**
     * TBD
     */
    private String[] initBaseDaoPackages;

    public boolean isAutoGenerateId() {
        return autoGenerateId;
    }

    public void setAutoGenerateId(boolean autoGenerateId) {
        this.autoGenerateId = autoGenerateId;
    }

    public String getAutoUpdateTimeStrategy() {
        return autoUpdateTimeStrategy;
    }

    public void setAutoUpdateTimeStrategy(String autoUpdateTimeStrategy) {
        this.autoUpdateTimeStrategy = autoUpdateTimeStrategy;
    }

    public boolean isInitData() {
        return initData;
    }

    public void setInitData(boolean initData) {
        this.initData = initData;
    }

    public String getInitBaseDir() {
        return initBaseDir;
    }

    public void setInitBaseDir(String initBaseDir) {
        this.initBaseDir = initBaseDir;
    }

    public String[] getInitBaseEntityPackages() {
        return initBaseEntityPackages;
    }

    public void setInitBaseEntityPackages(String[] initBaseEntityPackages) {
        this.initBaseEntityPackages = initBaseEntityPackages;
    }

    public String[] getInitBaseDaoPackages() {
        return initBaseDaoPackages;
    }

    public void setInitBaseDaoPackages(String[] initBaseDaoPackages) {
        this.initBaseDaoPackages = initBaseDaoPackages;
    }
}
