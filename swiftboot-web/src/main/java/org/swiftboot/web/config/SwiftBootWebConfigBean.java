package org.swiftboot.web.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;

/**
 * @author swiftech
 **/
@Configuration
@ConfigurationProperties("swiftboot.web")
public class SwiftBootWebConfigBean {

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
