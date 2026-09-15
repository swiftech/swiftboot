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

    private String formatPatternYearMonth = "yyyy-MM";

    private String formatPatternMonthDay = "MM-dd";

    private String formatPatternInstant = "yyyy-MM-dd HH:mm:ss";

    private String formatPatternOffsetDateTime = "yyyy-MM-dd HH:mm:ss";

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

    /**
     * Mock config
     */
    @NestedConfigurationProperty
    private MockConfigBean mock;

    /**
     * Rate limit config
     */
    @NestedConfigurationProperty
    private RateLimitConfigBean rateLimit = new RateLimitConfigBean();

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

    public String getFormatPatternInstant() {
        return formatPatternInstant;
    }

    public void setFormatPatternInstant(String formatPatternInstant) {
        this.formatPatternInstant = formatPatternInstant;
    }

    public String getFormatPatternOffsetDateTime() {
        return formatPatternOffsetDateTime;
    }

    public void setFormatPatternOffsetDateTime(String formatPatternOffsetDateTime) {
        this.formatPatternOffsetDateTime = formatPatternOffsetDateTime;
    }

    public String getFormatPatternYearMonth() {
        return formatPatternYearMonth;
    }

    public void setFormatPatternYearMonth(String formatPatternYearMonth) {
        this.formatPatternYearMonth = formatPatternYearMonth;
    }

    public String getFormatPatternMonthDay() {
        return formatPatternMonthDay;
    }

    public void setFormatPatternMonthDay(String formatPatternMonthDay) {
        this.formatPatternMonthDay = formatPatternMonthDay;
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

    public MockConfigBean getMock() {
        return mock;
    }

    public void setMock(MockConfigBean mock) {
        this.mock = mock;
    }

    public RateLimitConfigBean getRateLimit() {
        return rateLimit;
    }

    public void setRateLimit(RateLimitConfigBean rateLimit) {
        this.rateLimit = rateLimit;
    }
}
