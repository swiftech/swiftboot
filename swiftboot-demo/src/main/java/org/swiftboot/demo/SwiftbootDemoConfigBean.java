package org.swiftboot.demo;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.swiftboot.demo.config.InitConfigBean;

/**
 *
 * @author swiftech
 */
@Configuration
@ConfigurationProperties("swiftboot.demo")
public class SwiftbootDemoConfigBean {

    private InitConfigBean init = new InitConfigBean();

    public InitConfigBean getInit() {
        return init;
    }

    public void setInit(InitConfigBean init) {
        this.init = init;
    }
}
