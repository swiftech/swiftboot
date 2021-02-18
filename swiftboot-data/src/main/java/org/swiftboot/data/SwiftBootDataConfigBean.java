package org.swiftboot.data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.swiftboot.data.config.ModelConfigBean;

/**
 * @author swiftech
 **/
@ConfigurationProperties("swiftboot.data")
public class SwiftBootDataConfigBean {

    /**
     * Model 配置
     */
    @NestedConfigurationProperty
    private ModelConfigBean model = new ModelConfigBean();

    public ModelConfigBean getModel() {
        return model;
    }

    public void setModel(ModelConfigBean model) {
        this.model = model;
    }

}
