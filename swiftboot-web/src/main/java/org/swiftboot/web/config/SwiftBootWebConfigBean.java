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

    private String formatPatternLocalDateTime = "yyyy-MM-dd HH:mm:ss";

    private String formatPatternLocalDate = "yyyy-MM-dd";

    private String formatPatternLocalTime = "HH:mm:ss";

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

    public String getFormatPatternLocalDateTime() {
        return formatPatternLocalDateTime;
    }

    public void setFormatPatternLocalDateTime(String formatPatternLocalDateTime) {
        this.formatPatternLocalDateTime = formatPatternLocalDateTime;
    }

    public String getFormatPatternLocalDate() {
        return formatPatternLocalDate;
    }

    public void setFormatPatternLocalDate(String formatPatternLocalDate) {
        this.formatPatternLocalDate = formatPatternLocalDate;
    }

    public String getFormatPatternLocalTime() {
        return formatPatternLocalTime;
    }

    public void setFormatPatternLocalTime(String formatPatternLocalTime) {
        this.formatPatternLocalTime = formatPatternLocalTime;
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
