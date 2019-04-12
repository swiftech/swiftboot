package org.swiftboot.web;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.swiftboot.web.config.ModelConfigBean;
import org.swiftboot.web.config.ValidationResultConfigBean;

/**
 * @author Allen 2019-04-09
 **/
@ConfigurationProperties("swiftboot.web")
public class SwiftBootConfigBean {

    /**
     * Model 配置
     */
    @NestedConfigurationProperty
    private ModelConfigBean model;

    /**
     * 验证配置
     */
    @NestedConfigurationProperty
    private ValidationResultConfigBean validation;

    public ModelConfigBean getModel() {
        return model;
    }

    public void setModel(ModelConfigBean model) {
        this.model = model;
    }

    public ValidationResultConfigBean getValidation() {
        return validation;
    }

    public void setValidation(ValidationResultConfigBean validation) {
        this.validation = validation;
    }
}
