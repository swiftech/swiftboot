package org.swiftboot.demo.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.swiftboot.demo.exception.ErrorCode;
import org.swiftboot.web.exception.ErrMessageException;
import org.swiftboot.web.exception.ErrorCodeSupport;
import org.swiftboot.web.result.HttpResponse;
import org.swiftboot.web.result.ResponseBuilder;

/**
 * @author swiftech
 **/
@Api("Test测试")
@Controller
@RequestMapping("/test")
public class TestController {

    private final Logger log = LoggerFactory.getLogger(TestController.class);

    @ApiOperation(notes = "测试资源", value = "测试资源")
    @RequestMapping(value = "message/resource/code", method = RequestMethod.GET)
    public
    @ResponseBody
    HttpResponse<Void> testMessageResource() {
        throw new ErrMessageException(ErrorCodeSupport.CODE_SYS_ERR);
    }

    @ApiOperation(notes = "测试资源", value = "测试资源")
    @RequestMapping(value = "message/resource/customize", method = RequestMethod.GET)
    public
    @ResponseBody
    HttpResponse<Void> testMessageResourceCustomize() {
        throw new ErrMessageException(ErrorCodeSupport.CODE_SYS_ERR, "自定义错误消息");
    }

    @ApiOperation(notes = "测试资源", value = "测试资源")
    @RequestMapping(value = "message/resource/params", method = RequestMethod.GET)
    public
    @ResponseBody
    HttpResponse<Void> testMessageResourceParams() {
        return new ResponseBuilder<Void>().code(ErrorCode.CODE_TEST_PARAMS).msgParams("foo", "bar").build();
    }

    @ApiOperation(notes = "测试资源", value = "测试资源")
    @RequestMapping(value = "message/resource/customize/params", method = RequestMethod.GET)
    public
    @ResponseBody
    HttpResponse<Void> testMessageResourceCustomizeParams() {
        return new ResponseBuilder<Void>().code(ErrorCode.CODE_TEST_PARAMS).msg("自定义错误消息：{0}和{1}").msgParams("foo", "bar").build();
    }
}
