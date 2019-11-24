package org.swiftboot.demo.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.swiftboot.web.exception.ErrMessageException;
import org.swiftboot.web.exception.ErrorCodeSupport;
import org.swiftboot.web.result.HttpResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author swiftech
 **/
@Api("Test测试")
@Controller
@RequestMapping("/test")
public class TestController {

    private Logger log = LoggerFactory.getLogger(TestController.class);


    @ApiOperation(notes = "测试资源", value = "测试资源")
    @RequestMapping(value = "message/resource", method = RequestMethod.GET)
    public
    @ResponseBody
    HttpResponse<Void> testMessageResource(
            HttpServletResponse response,
            HttpServletRequest request) {
        throw new ErrMessageException(ErrorCodeSupport.CODE_SYS_ERR);
    }
}
