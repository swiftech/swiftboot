package org.swiftboot.web.config;

/**
 * @author swiftech
 **/
public class ModelConfigBean {

    /**
     * 是否自动生成主键，默认为false
     */
    private boolean autoGenerateId = false;

    public boolean isAutoGenerateId() {
        return autoGenerateId;
    }

    public void setAutoGenerateId(boolean autoGenerateId) {
        this.autoGenerateId = autoGenerateId;
    }

}
