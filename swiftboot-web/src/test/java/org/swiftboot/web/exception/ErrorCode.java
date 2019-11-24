package org.swiftboot.web.exception;

import org.springframework.stereotype.Component;

/**
 * @author Allen 2019-03-05
 **/
@Component
public class ErrorCode extends ErrorCodeSupport {

    @Override
    public void init() {
        initErrorCode();
    }
}
