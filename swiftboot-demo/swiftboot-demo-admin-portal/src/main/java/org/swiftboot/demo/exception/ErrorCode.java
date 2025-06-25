package org.swiftboot.demo.exception;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import org.swiftboot.web.response.ResponseCode;

/**
 * @author swiftech 2019-03-05
 **/
@Component
public class ErrorCode extends ResponseCode {

    public static final String CODE_TEST_PARAMS = "4001";

    @PostConstruct
    public void init() {
        try {
            super.loadFromClass(this.getClass());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
