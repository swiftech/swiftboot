package org.swiftboot.data.config;

import org.springframework.boot.context.properties.NestedConfigurationProperty;
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
     * 服务节点编号（用于分布式服务生成主键ID），默认为 0 ，当 autoGenerateId = true 时才有效
     */
    private int serverNode = 0;

    /**
     * 是否自动设置更新时间，可选"not-set"，"on-change"，"always"
     */
    private String autoUpdateTimeStrategy = AutoUpdateTimeStrategy.AUTO_UPDATE_TIME_ON_CHANGE;

    /**
     * 初始化数据
     */
    @NestedConfigurationProperty
    private InitDataConfigBean initData;

    public boolean isAutoGenerateId() {
        return autoGenerateId;
    }

    public void setAutoGenerateId(boolean autoGenerateId) {
        this.autoGenerateId = autoGenerateId;
    }

    public int getServerNode() {
        return serverNode;
    }

    public void setServerNode(int serverNode) {
        this.serverNode = serverNode;
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
