package org.swiftboot.demo.exception;

import org.springframework.stereotype.Component;
import org.swiftboot.web.exception.ErrorCodeSupport;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @author swiftech 2019-03-05
 **/
@Component
public class ErrorCode {

    public static final String CODE_TEST_PARAMS = "4001";

    @Resource
    private ErrorCodeSupport errorCodeSupport;

    @PostConstruct
    public void init() {
        try {
            errorCodeSupport.loadFromClass(this.getClass());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
