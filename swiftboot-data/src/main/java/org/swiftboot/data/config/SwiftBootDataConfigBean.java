package org.swiftboot.data.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;

/**
 * @author swiftech
 **/
@Configuration
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
