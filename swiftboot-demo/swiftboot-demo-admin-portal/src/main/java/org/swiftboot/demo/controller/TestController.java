package org.swiftboot.demo.controller;

import org.swiftboot.demo.exception.ErrorCode;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.swiftboot.web.exception.ErrMessageException;
import org.swiftboot.web.exception.ErrorCodeSupport;
import org.swiftboot.web.dto.HttpResponse;
import org.swiftboot.web.dto.ResponseBuilder;

/**
 * @author swiftech
 **/
@Tag(name = "Test测试")
@Controller
@RequestMapping("/test")
public class TestController {

    private static final Logger log = LoggerFactory.getLogger(TestController.class);

    @Operation(description = "测试资源", value = "测试资源")
    @RequestMapping(value = "message/resource/code", method = RequestMethod.GET)
    public
    @ResponseBody
    HttpResponse<Void> testMessageResource() {
        throw new ErrMessageException(ErrorCodeSupport.CODE_SYS_ERR);
    }

    @Operation(description = "测试资源", value = "测试资源")
    @RequestMapping(value = "message/resource/customize", method = RequestMethod.GET)
    public
    @ResponseBody
    HttpResponse<Void> testMessageResourceCustomize() {
        throw new ErrMessageException(ErrorCodeSupport.CODE_SYS_ERR, "自定义错误消息");
    }

    @Operation(description = "测试资源", value = "测试资源")
    @RequestMapping(value = "message/resource/params", method = RequestMethod.GET)
    public
    @ResponseBody
    HttpResponse<Void> testMessageResourceParams() {
        return new ResponseBuilder<Void>().code(ErrorCode.CODE_TEST_PARAMS).msgParams("foo", "bar").build();
    }

    @Operation(description = "测试资源", value = "测试资源")
    @RequestMapping(value = "message/resource/customize/params", method = RequestMethod.GET)
    public
    @ResponseBody
    HttpResponse<Void> testMessageResourceCustomizeParams() {
        return new ResponseBuilder<Void>().code(ErrorCode.CODE_TEST_PARAMS).msg("自定义错误消息：{0}和{1}").msgParams("foo", "bar").build();
    }
}
