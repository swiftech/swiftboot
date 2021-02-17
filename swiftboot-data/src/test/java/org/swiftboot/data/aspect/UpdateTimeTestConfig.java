package org.swiftboot.data.aspect;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.swiftboot.data.SwiftBootDataConfig;

/**
 * @author swiftech
 **/
@Configuration
@Import({
        SwiftBootDataConfig.class
})
public class UpdateTimeTestConfig {

}
