package org.swiftboot.demo.exception;

import org.springframework.stereotype.Component;
import org.swiftboot.web.exception.ErrorCodeSupport;

/**
 * @author swiftech 2019-03-05
 **/
@Component
public class ErrorCode extends ErrorCodeSupport {

    @Override
    public void init() {
//        System.out.println("开始初始化错误代码...");
        initErrorCode();
    }
}
