package org.swiftboot.demo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author swiftech
 */
@Configuration
@ConfigurationProperties("swiftboot.demo")
public class SwiftbootDemoConfigBean {

    @NestedConfigurationProperty
    private InitConfigBean init = new InitConfigBean();

    public InitConfigBean getInit() {
        return init;
    }

    public void setInit(InitConfigBean init) {
        this.init = init;
    }
}
