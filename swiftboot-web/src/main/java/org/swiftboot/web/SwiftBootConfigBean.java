package org.swiftboot.web;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.swiftboot.web.config.FilterConfigBean;
import org.swiftboot.web.config.ModelConfigBean;
import org.swiftboot.web.config.ValidationResultConfigBean;

/**
 * @author swiftech
 **/
@ConfigurationProperties("swiftboot.web")
public class SwiftBootConfigBean {

    /**
     * Model 配置
     */
    @NestedConfigurationProperty
    private ModelConfigBean model = new ModelConfigBean();

    /**
     * 验证配置
     */
    @NestedConfigurationProperty
    private ValidationResultConfigBean validation = new ValidationResultConfigBean();

    /**
     * 过滤器配置
     */
    @NestedConfigurationProperty
    private FilterConfigBean filter = new FilterConfigBean();

//    @Bean
//    public CorsFilter corsFilter() {
//        return new CorsFilter();
//    }

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

    public FilterConfigBean getFilter() {
        return filter;
    }

    public void setFilter(FilterConfigBean filter) {
        this.filter = filter;
    }
}
