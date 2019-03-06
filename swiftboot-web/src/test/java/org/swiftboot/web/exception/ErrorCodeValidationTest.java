package org.swiftboot.web.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.context.MessageSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.annotation.Resource;

/**
 * @author Allen 2019-03-05
 **/
@ExtendWith(SpringExtension.class)
@ContextConfiguration({"/error_code_test.xml"})
public class ErrorCodeValidationTest {


    @Resource
    MessageSource messageSource;

    @Resource
    ErrorCode errorCode;

    @Test
    public void test() {
        System.out.println(errorCode);
        System.out.println(ErrorCode.getErrorMessage(ErrorCode.CODE_SYS_ERR));
    }

}
