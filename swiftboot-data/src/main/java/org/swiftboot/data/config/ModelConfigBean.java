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
     * 初始化数据
     */
    private InitDataConfigBean initData;

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

    public InitDataConfigBean getInitData() {
        return initData;
    }

    public void setInitData(InitDataConfigBean initData) {
        this.initData = initData;
    }
}
